package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import com.lookout.keymaster.R;

/**
 * Created with IntelliJ IDEA.
 * User: ayerra
 * Date: 5/29/13
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class SendSignedFragment extends Fragment {
    SimpleAdapter adapter;

    public SendSignedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_send_signed, container, false);

        getActivity().setTitle("Home");
        return rootView;
    }
}