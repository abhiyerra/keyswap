package com.lookout.gpg;

import android.app.ListActivity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.*;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class KeyActivity extends SlidingActivity implements NfcAdapter.CreateNdefMessageCallback {
    NfcAdapter mNfcAdapter;

    ArrayList<Map<String, String>> keys;
    SimpleAdapter adapter;

    public static String TRUST_DONT_KNOW          = "1";
    public static String TRUST_I_DO_NOT_TRUST     = "2";
    public static String TRUST_I_TRUST_MARGINALLY = "3";
    public static String TRUST_I_TRUST_FULLY      = "4";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GPGBinding gpg = new GPGCli();
        gpg.ImportKey("/sdcard/key.asc");
        gpg.GetKeys();

        }

        buildData();

        ListView lv = (ListView) findViewById(R.id.keyView);
        String[] from = { "full_name", "pgp_fingerprint" };
        int[] to = { R.id.full_name, R.id.pgp_fingerprint };

        adapter = new SimpleAdapter(this, keys,
                R.layout.key_list_item, from, to);
        lv.setAdapter(adapter);









        setBehindContentView(R.layout.menu);
        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        setSlidingActionBarEnabled(true);


        ListView lv2 = (ListView) findViewById(R.id.main_menu_list);
        String[] values = new String[] {
                "Home",
                "Exchange Keys",
                "Sync Keyring",
                "Public Keys",
                "Private Keys"
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, values);
        lv2.setAdapter(adapter2);


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this,this);
    }

    private void buildData() {
        keys = new ArrayList<Map<String, String>>();
        keys.add(putData("Abhi Yerra", "EC92C369", "1"));
        keys.add(putData("Shane Wilton", "02C834B6", "2"));
        keys.add(putData("Derek Halliday", "33D8457A", "3"));
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

        String text =("Hi Shane!\n\n"+
                "Beam Time: "+System.currentTimeMillis());

        /*
        try {
            Process p = Runtime.getRuntime().exec("/data/data/info.guardianproject.gpg/app_opt/aliases/gpg --list-keys");
            BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));

            text = bf.readLine();
        } catch(IOException e) {

        }
        */

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
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // record 0 contains the MIME type, record 1 is the AAR, if present
        String messageReceived = new String(msg.getRecords()[0].getPayload());

        keys.add(putData(messageReceived, "",""));

        adapter.notifyDataSetChanged();

        Toast.makeText(this, messageReceived, Toast.LENGTH_LONG).show();
    }

}
