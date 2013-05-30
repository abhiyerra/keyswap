package com.lookout.gpg;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class KeyTrustLevelFragment extends Fragment {
    public KeyTrustLevelFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_key_trust_level, container, false);

        Button signButton = (Button)rootView.findViewById(R.id.sign_key_button);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getActivity().setTitle("Key Exchange");
        return rootView;
    }
}

