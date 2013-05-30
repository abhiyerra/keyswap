package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.lookout.keymaster.gpg.GPGFactory;
import com.lookout.keymaster.R;

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
