package com.lookout.gpg;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: ayerra
 * Date: 5/29/13
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class HomeFragment extends Fragment {
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        GPGFactory.buildData();

        ListView lv = (ListView) rootView.findViewById(R.id.keyView);
        String[] from = { "full_name", "key_id" };
        int[] to = { R.id.full_name, R.id.key_id };
        adapter = new SimpleAdapter(rootView.getContext(), GPGFactory.getKeys(), R.layout.key_list_item, from, to);
        lv.setAdapter(adapter);           */

        getActivity().setTitle("Home");
        return rootView;
    }
}
