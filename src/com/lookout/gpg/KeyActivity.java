package com.lookout.gpg;

import android.app.ListActivity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyActivity extends ListActivity implements NfcAdapter.CreateNdefMessageCallback {
    NfcAdapter mNfcAdapter;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        ArrayList<Map<String, String>> list = buildData();
        String[] from = { "full_name", "pgp_fingerprint" };
        int[] to = { R.id.full_name, R.id.pgp_fingerprint };

        SimpleAdapter adapter = new SimpleAdapter(this, list,
                R.layout.key_list_item, from, to);
        setListAdapter(adapter);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this,this);
    }

    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(putData("Abhi Yerra", "1024D/EC92C369", "1"));
        list.add(putData("Shane Wilton", "1024D/02C834B6", "2"));
        list.add(putData("Derek Halliday", "1024D/33D8457A", "3"));
        return list;
    }

    private HashMap<String, String> putData(String name, String pgp_fingerprint, String trust_level) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("full_name", name);
        item.put("pgp_fingerprint", pgp_fingerprint);
        item.put("trust_level", trust_level);
        return item;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event){
        String text =("This is an encrypted item being sent!\n\n"+
                "Beam Time: "+System.currentTimeMillis());

        NdefMessage msg = new NdefMessage(NdefRecord.createMime("application/vnd.com.example.android.beam", text.getBytes()));
            /**
             * The Android Application Record (AAR) is commented out. When a device
             * receives a push with an AAR in it, the application specified in the AAR
             * is guaranteed to run. The AAR overrides the tag dispatch system.
             * You can add it back in to guarantee that this
             * activity starts when receiving a beamed message. For now, this code
             * uses the tag dispatch system.
             */
            //,NdefRecord.createApplicationRecord("com.example.android.beam")

        return msg;
    }

    @Override
    public void onResume(){
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent){

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg =(NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        Toast.makeText(this, new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
    }

}
