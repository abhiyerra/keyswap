package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.lookout.keymaster.gpg.GPGFactory;
import com.lookout.keymaster.R;

public class KeyVerifyFragment extends Fragment {
    SimpleAdapter adapter;

    View rootView;

    public KeyVerifyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_key_verify, container, false);

        setTextForId(R.id.their_email, "");
        setTextForId(R.id.their_full_name, "Abhi Yerra");
        setTextForId(R.id.their_short_id, "1234");
        setTextForId(R.id.their_created, "");


        Button verifyButton = (Button)rootView.findViewById(R.id.verify_key_button);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new KeyTrustLevelFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "key_trust_level").commit();
            }
        });

        getActivity().setTitle("Key Exchange");
        return rootView;
    }

    private void setTextForId(int id, String txt) {
        TextView tv = (TextView)rootView.findViewById(id);
        tv.setText(txt);
    }

}
