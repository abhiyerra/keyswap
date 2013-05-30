package com.lookout.gpg;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.*;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class KeyActivity extends Activity {

    ListView lv2;

    Fragment fragment;
    FragmentManager fragmentManager;

    private DrawerLayout mDrawerLayout;


    public static String TRUST_DONT_KNOW          = "1";
    public static String TRUST_I_DO_NOT_TRUST     = "2";
    public static String TRUST_I_TRUST_MARGINALLY = "3";
    public static String TRUST_I_TRUST_FULLY      = "4";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fragmentManager = getFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        GPGBinding gpg = new GPGCli();
        gpg.ImportKey("/sdcard/key.asc");
        List<GPGKey> keys = gpg.GetKeys();

        setupSidebar();

        loadKeyFragment(true);
    }



    private void setupSidebar() {
        lv2 = (ListView) findViewById(R.id.left_drawer);
        String[] values = new String[] {
                "Home",
                "Exchange Keys",
                "Sync Keyring",
                "Public Keys",
                "Private Keys"
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, values);
        lv2.setOnItemClickListener(new DrawerItemClickListener());

        lv2.setAdapter(adapter2);
    }

    private void loadHomeFragment() {
        fragment = new HomeFragment();
        Bundle args = new Bundle();
        //args.putInt(KeyFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "home").commit();
    }

    private void loadExchangeFragment() {
        fragment = new ExchangeFragment();
        Bundle args = new Bundle();
        //args.putInt(KeyFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "exchange").commit();
    }

    private void loadKeyFragment(boolean forPublicKeys) {
        fragment = new KeyFragment();
        Bundle args = new Bundle();
        args.putBoolean("ForPublicKeys", forPublicKeys);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "key").commit();
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0:
                    loadHomeFragment();
                    break;
                case 1:
                    loadExchangeFragment();
                    break;
                case 3:
                    loadKeyFragment(true);
                    break;
                case 4:
                    loadKeyFragment(false);
                    break;
            }


            // update selected item and title, then close the drawer
            lv2.setItemChecked(position, true);
            //setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(lv2);
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        // onResume gets called after this to handle the intent
        this.setIntent(intent);
    }


}
