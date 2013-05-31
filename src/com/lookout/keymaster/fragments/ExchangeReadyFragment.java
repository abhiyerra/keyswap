package com.lookout.keymaster.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.lookout.keymaster.R;
import com.lookout.keymaster.gpg.GPGCli;
import com.lookout.keymaster.gpg.GPGFactory;
import com.lookout.keymaster.gpg.GPGKey;

public class ExchangeReadyFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_exchange_ready, container, false);

        String sendingKeyId = GPGFactory.getPublicKeyId();

        new SendingKeyTask(sendingKeyId).execute();



        getActivity().setTitle("Key Exchange");
        return rootView;
    }

    private void setTextForId(int id, String txt) {
        TextView tv = (TextView)rootView.findViewById(id);
        tv.setText(txt);
    }


    private class SendingKeyTask extends AsyncTask<Void, Void, GPGKey> {
        String keyId;

        public SendingKeyTask(String keyId)  {
            this.keyId = keyId;
        }

        protected GPGKey doInBackground(Void... voids) {
            return GPGCli.getInstance().getPublicKey(keyId);
        }

        protected void onPostExecute(GPGKey result) {
            setTextForId(R.id.sending_short_id, result.getShortId());
            setTextForId(R.id.sending_full_name, result.getPrimaryKeyId().getPersonalName());
            setTextForId(R.id.sending_email, result.getPrimaryKeyId().getEmail());
        }
    }
}