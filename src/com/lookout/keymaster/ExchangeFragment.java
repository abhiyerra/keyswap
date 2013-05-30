package com.lookout.keymaster;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.lookout.keymaster.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExchangeFragment extends Fragment {

    SimpleAdapter adapter;


    public ExchangeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exchange, container, false);

        GPGFactory.buildData();

        ListView lv = (ListView) rootView.findViewById(R.id.keyToShare);
        String[] from = { "full_name", "key_id" };
        int[] to = { R.id.full_name, R.id.short_id };
        adapter = new SimpleAdapter(rootView.getContext(), GPGFactory.getKeys(), R.layout.key_list_item, from, to);
        lv.setAdapter(adapter);

        getActivity().setTitle("Key Exchange");

        return rootView;
    }



}
