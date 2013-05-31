package com.lookout.keymaster;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.lookout.keymaster.fragments.*;
import com.lookout.keymaster.gpg.GPGCli;
import com.lookout.keymaster.gpg.GPGFactory;
import com.lookout.keymaster.gpg.KeyringSyncManager;

public class MainActivity extends Activity  implements NfcAdapter.CreateNdefMessageCallback {

    private static boolean firstLoad = true;

    NfcAdapter mNfcAdapter;

    ListView lv2;

    Fragment fragment;
    FragmentManager fragmentManager;

    private DrawerLayout mDrawerLayout;

    public static String TRUST_DONT_KNOW          = "1";
    public static String TRUST_I_DO_NOT_TRUST     = "2";
    public static String TRUST_I_TRUST_MARGINALLY = "3";
    public static String TRUST_I_TRUST_FULLY      = "4";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        fragmentManager = getFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setupSidebar();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
        } else {
            // Register callback
            mNfcAdapter.setNdefPushMessageCallback(this, this);
        }

        if(firstLoad) {
            loadFragment(new HomeFragment());
            firstLoad = false;
        }
    }


    private void setupSidebar() {
        lv2 = (ListView) findViewById(R.id.left_drawer);
        String[] values = new String[] {
                "Home",
                "Exchange Keys",
                "Sync Keyring",
                "Public Keys",
                "Private Keys"
        };

        MenuArrayAdapter adapter2 = new MenuArrayAdapter(this, values);
        lv2.setAdapter(adapter2);
        lv2.setOnItemClickListener(new DrawerItemClickListener());


    }

    private void loadFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0:
                    loadFragment(new HomeFragment());
                    break;
                case 1:
                    loadFragment(new ExchangeFragment());
                    break;
                case 2:
                    new KeyringSyncTask().execute();
                    break;
                case 3:
                    loadFragment(new PublicKeysFragment());
                    break;
                case 4:
                    loadFragment(new PrivateKeysFragment());
                    break;
            }


            // update selected item and title, then close the drawer
            lv2.setItemChecked(position, true);
            //setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(lv2);
        }
    }


    private class KeyringSyncTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... voids) {
            KeyringSyncManager.getInstance().sync(getApplicationContext());

            return null;
        }

        // Have a UI handler to show progress of the import
        /*
        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }
*/
        protected void onPostExecute(Void result) {
            //loadKeyFragment(true);
        }

    }

    @Override
    public void onNewIntent(Intent intent){
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event){
        NdefMessage msg = new NdefMessage(
                NdefRecord.createMime("application/vnd.com.lookout.keymaster.beam", GPGFactory.getPublicKey().getBytes()),
                NdefRecord.createMime("application/vnd.com.lookout.keymaster.beam", GPGFactory.getPublicKeyId().getBytes()),
                NdefRecord.createMime("application/vnd.com.lookout.keymaster.beam", GPGFactory.getSignedKey().getBytes()),
                NdefRecord.createApplicationRecord("com.lookout.keymaster"));

        return msg;
    }

    @Override
    public void onResume(){
        super.onResume();
        // Check to see that the Activity started due to an Android Beam

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(this.getIntent().getAction())){
            processIntent(this.getIntent());
        }
    }

    void processIntent(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // record 0 contains the MIME type, record 1 is the AAR, if present
        String receivedKey = new String(msg.getRecords()[0].getPayload());
        String receivedKeyId = new String(msg.getRecords()[1].getPayload());
        String signedKey = new String(msg.getRecords()[2].getPayload());

        Log.i("KeyMaster", "receivedmsg" + receivedKey);

        new AddKeyToKeychainTask(receivedKey, receivedKeyId, signedKey).execute();
    }

    private class AddKeyToKeychainTask extends AsyncTask<Void, Void, Void> {
        String keyArmor, keyId;
        String signedKey;

        public AddKeyToKeychainTask(String keyArmor, String keyId, String signedKey)  {
            this.keyArmor = keyArmor;
            this.keyId = keyId;
            this.signedKey = signedKey;
        }

        protected Void doInBackground(Void... voids) {
            GPGFactory.setReceivedKey(keyArmor, keyId);
            GPGCli.getInstance().importAsciiArmoredKey(this.signedKey);
            GPGFactory.buildKeyPairList();

            return null;
        }

        protected void onPostExecute(Void result) {
            loadFragment(new KeyVerifyFragment());
        }
    }
}
