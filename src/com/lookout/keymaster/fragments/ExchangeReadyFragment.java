package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.lookout.keymaster.R;
import com.lookout.keymaster.gpg.GPGFactory;

public class ExchangeReadyFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_exchange_ready, container, false);

        String sendingKeyId = GPGFactory.getPublicKeyId();

        setTextForId(R.id.sending_short_id, sendingKeyId);
        setTextForId(R.id.sending_created, "");
        setTextForId(R.id.sending_email, "email");
        setTextForId(R.id.sending_full_name, "full");

        getActivity().setTitle("Key Exchange");
        return rootView;
    }

    private void setTextForId(int id, String txt) {
        TextView tv = (TextView)rootView.findViewById(id);
        tv.setText(txt);
    }
}