package com.lookout.gpg;

import android.app.Fragment;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExchangeFragment extends Fragment implements NfcAdapter.CreateNdefMessageCallback {
    SimpleAdapter adapter;

    NfcAdapter mNfcAdapter;

    public ExchangeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exchange, container, false);

        GPGFactory.buildData();

        ListView lv = (ListView) rootView.findViewById(R.id.keyToShare);
        String[] from = { "full_name", "pgp_fingerprint" };
        int[] to = { R.id.full_name, R.id.short_id };
        adapter = new SimpleAdapter(rootView.getContext(), GPGFactory.getKeys(), R.layout.key_list_item, from, to);
        lv.setAdapter(adapter);

        getActivity().setTitle("Key Exchange");


        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity().getApplicationContext());
        if(mNfcAdapter == null){
            Toast.makeText(getActivity(), "NFC is not available", Toast.LENGTH_LONG).show();
        } else {
            // Register callback
            mNfcAdapter.setNdefPushMessageCallback(this, getActivity());
        }

        return rootView;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event){

        String text =("Hi Shane!\n\n"+
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

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getActivity().getIntent().getAction())){
            processIntent(getActivity().getIntent());
        }
    }



    void processIntent(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // record 0 contains the MIME type, record 1 is the AAR, if present
        String messageReceived = new String(msg.getRecords()[0].getPayload());

        GPGFactory.addKey(messageReceived);

        Toast.makeText(getActivity().getApplicationContext(), messageReceived, Toast.LENGTH_LONG).show();
    }
}