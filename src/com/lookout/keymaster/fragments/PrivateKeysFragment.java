package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.lookout.keymaster.R;
import com.lookout.keymaster.gpg.GPGFactory;

public class PrivateKeysFragment extends Fragment {
    SimpleAdapter adapter;

    public PrivateKeysFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_key, container, false);

        GPGFactory.buildPrivateKeyList();

        ListView lv = (ListView) rootView.findViewById(R.id.keyView);
        String[] from = { "full_name", "key_id" };
        int[] to = { R.id.full_name, R.id.short_id };
        adapter = new SimpleAdapter(rootView.getContext(), GPGFactory.getKeys(), R.layout.key_list_item, from, to);
        lv.setAdapter(adapter);

        getActivity().setTitle("Private Keys");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
