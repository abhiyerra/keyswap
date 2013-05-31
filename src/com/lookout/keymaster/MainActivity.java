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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.lookout.keymaster.fragments.*;
import com.lookout.keymaster.gpg.GPGFactory;
import com.lookout.keymaster.gpg.KeyringSyncManager;

public class MainActivity extends Activity  implements NfcAdapter.CreateNdefMessageCallback {

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

        loadFragment(new HomeFragment());
    }


    private void setupSidebar() {
        lv2 = (ListView) findViewById(R.id.left_drawer);
        String[] values = new String[] {
                "Home",
                "Exchange Keys",
                "Sync Keyring",
                "Public Keys",
                "Private Keys",
                "READY TO VERIFY",
                "SERVER PUSH"
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
                    loadFragment(new KeyFragment());
                    break;
                case 4:
                    loadFragment(new KeyFragment());
                    break;
                case 5:       // Only for TESTING.

                    String fakeKey =
                            "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
                                    "Version: GnuPG v1.4.13 (Darwin)\n" +
                                    "Comment: GPGTools - http://gpgtools.org\n" +
                                    "\n" +
                                    "mQENBFGn314BCAC0pa26XqsEUAwX3WQaReD6WpZauEPsWoefAlLaX43F8eS1QLyG\n" +
                                    "art+n5IBb5OcnZpDIl/qPlct02wu/bf5eLOe3OfGjgISB27Yq01OYEwQJ7cYVWIg\n" +
                                    "+C79tI1ApoXlqj1Dai7nyNCJS444PF1o3Vj9ILNZXAlfSIxlPG6bgdsLTLm0SB2Z\n" +
                                    "J1BC6bBLE38wKBuu7/wGhHG9+euWratnh24dCqYj3opImnmaFQen7znUUNx7XSOR\n" +
                                    "BQUpuUPS8cYgx6hLTc2SkC0FfKnESSeVGEfFtGQL1dLmSpw4zZrhk5rYs3LQ5ZBN\n" +
                                    "X9/1WMLWG1EqV/pdN/LCyttVdgBBnEd8E69vABEBAAG0JlRlc3QgTG9va291dCA8\n" +
                                    "dGVzdGxvb2tvdXRAZXhhbXBsZS5jb20+iQE+BBMBAgAoBQJRp99eAhsDBQkADS8A\n" +
                                    "BgsJCAcDAgYVCAIJCgsEFgIDAQIeAQIXgAAKCRC26aFtFfrpb+6BCACDbX716RXi\n" +
                                    "9LUk/LJW6onk+ASMoJE/3MYCvzJ476PDQ0CX6ZXpdLijn7TBzpOxYmgKfF+DdV1t\n" +
                                    "svd6JN6VGCZi7B6IxZZ2tLSnYmyLXLc+ZrFU9+iYKUqWjeuVHDNv2m2Vtj9IP4QW\n" +
                                    "vwkslrhoJZimUkwph0YXzmU3UvyXYs06HSykqTS+5V85eHs7MZZi/5yqo3/Zn3yE\n" +
                                    "32E5ViDtU4uChAkKvNkx4tCLO/Watv1wPLIvNYNLgGdkq7IGOSoHo+GWRUqrgGxa\n" +
                                    "p2kMZbDnSAA7Uml8AfF3ALyGtMmHIdyGzRZIGUcqN8JXmz54qDwXEDzf8BO+bHYg\n" +
                                    "e4Cfd+ak1GgauQENBFGn314BCADQLZ0+vtZuzYqO8vtS8/dziTH0yntxglqZ0EAR\n" +
                                    "R7u1jiGaKdlgRmjQVFg+vffPmjnSKA63DDOz3Bu+SAODXAiuEiSlSR4oAJ02PPDS\n" +
                                    "FtdBvNE6C62ids1W+jrif74OWoU2+44bYqML8YCuy0lWMQSMWQZdcbH6K4S9IxCr\n" +
                                    "ooWFAYcweWhb0rAgYj4TjdayV5Pdhd9zS2V6uqcNkZxNmJ54huDSZ4mILmi7XO4s\n" +
                                    "GYvFSuycYNRLxMyYpUcityjc0mP9Zo9zixQE5CL5yn6LfrtIG/+gsCaKBzIOIhqZ\n" +
                                    "WyDEJFclwqgr8YsWbw7QheRmdmXQCUa14UzfyyBC1wdx425xABEBAAGJASUEGAEC\n" +
                                    "AA8FAlGn314CGwwFCQANLwAACgkQtumhbRX66W8FLQgAlgrQoGUb5xAT1cqbmzw/\n" +
                                    "x/v1MiU24MdP4qan0qqGExnHQ7u+h6xwX4in8Iv1fdEwPRyC7M9xfmx2N7K3L9o2\n" +
                                    "KrgEbOlKpIINGMe4F1vSY+66VlPV0oUrimIhZ71uO3so5+84t3pHdMTP7h4qUEJI\n" +
                                    "IQ5r9JXHY8ett/9G4hDlEvPyR5FS5erEaHbmAPQY9bxVnWbThbHWBwkl7PHcsvjk\n" +
                                    "zP6YDzT1GCT8Z+vyEUfOUN+jjsKaK0qfMBCjkQyXFduJavj7FY1K5SdI85IIUBA0\n" +
                                    "uCo4Stm/iZQ1Q0ORWn/gCJbImBrygu4FHpX9Vg8qDZBMqEPWotdBTQILXjqGbcdk\n" +
                                    "Ag==\n" +
                                    "=cptb\n" +
                                    "-----END PGP PUBLIC KEY BLOCK-----";

                    GPGFactory.setPublicKey("67280F23EC92C369");
                    GPGFactory.setReceivedKey(fakeKey, "B6E9A16D15FAE96F");
                    loadFragment(new KeyVerifyFragment());
                    break;
                case 6:
                    loadFragment(new ServerPushFragment());
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
                //NdefRecord.createMime("application/vnd.com.lookout.keymaster.beam", GPGFactory.getSignedKey().getBytes()),
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

        Log.i("KeyMaster", "receivedmsg" + receivedKey);

        new AddKeyToKeychainTask(receivedKey, receivedKeyId).execute();
    }

    private class AddKeyToKeychainTask extends AsyncTask<Void, Void, Void> {
        String keyArmor, keyId;

        public AddKeyToKeychainTask(String keyArmor, String keyId)  {
            this.keyArmor = keyArmor;
            this.keyId = keyId;
        }

        protected Void doInBackground(Void... voids) {
            GPGFactory.setReceivedKey(keyArmor, keyId);
            GPGFactory.buildData();

            return null;
        }

        protected void onPostExecute(Void result) {
            loadFragment(new KeyVerifyFragment());
        }
    }
}
