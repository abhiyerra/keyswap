package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.lookout.keymaster.gpg.GPGFactory;
import com.lookout.keymaster.R;
import com.lookout.keymaster.gpg.KeyringSyncManager;

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

        final ListView lv = (ListView) rootView.findViewById(R.id.keyToShare);
        String[] from = { "full_name", "key_id", "", "" };
        int[] to = { R.id.full_name, R.id.short_id, R.id.email, R.id.created };
        adapter = new SimpleAdapter(rootView.getContext(), GPGFactory.getKeys(), R.layout.key_list_item, from, to);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lv.getItemAtPosition(i);
                Map<String, String> keyAtPosition = GPGFactory.getKeys().get(i);
                String pgp_key_id = keyAtPosition.get("key_id");

                new SelectKeyTask(pgp_key_id).execute();
            }
        });

        final TextView tv = (TextView) rootView.findViewById(R.id.exchangePrompt);
        tv.setText(R.string.exchange_select_prompt);

        getActivity().setTitle(R.string.exchange);

        return rootView;
    }

    private class SelectKeyTask extends AsyncTask<Void, Void, Void> {
        String keyId;

        public SelectKeyTask(String keyId)  {
            this.keyId = keyId;
        }

        protected Void doInBackground(Void... voids) {
            GPGFactory.setPublicKey(keyId);

            return null;
        }

        protected void onPostExecute(Void result) {
            Fragment fragment = new ExchangeReadyFragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "exchange_ready").commit();
        }

    }

}
