package com.lookout.keymaster.gpg;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GPGFactory {
    public static ArrayList<Map<String, String>> keys;

    public static String publicKey, publicKeyId;
    public static String receivedKey, receivedKeyId;

    public static ArrayList<Map<String, String>> getKeys() {
        return keys;
    }

    public static void buildData() {
        keys = new ArrayList<Map<String, String>>();

        ArrayList<GPGKey> publicKeys = GPGCli.getInstance().getPublicKeys();
        for(GPGKey key : publicKeys) {
        //ArrayList<GPGKeyPair> keyPairs = GPGCli.getInstance().getKeyPairs();

        //for(GPGKeyPair keypair : keyPairs) {
            //GPGKey key = keypair.getPublicKey();

            keys.add(putData(key.getPrimaryKeyId().getUserId(), key.getKeyId(), key.getParentKey().getOwnerTrust().toString()));
        }
    }

    public static HashMap<String, String> putData(String name, String key_id, String trust_level) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("full_name", name);
        item.put("key_id", key_id);
        item.put("trust_level", trust_level);
        return item;
    }


    public static HashMap<String, String> getKeyByKeyId(String key_id) {

        HashMap<String, String> x = new HashMap<String, String>();

        return x;
    }

    public static void setReceivedKey(String key, String key_id) {
        receivedKey = key;
        receivedKeyId = key_id;
        GPGCli.getInstance().importAsciiArmoredKey(key);
        buildData();
    }

    public static String getReceivedKey() {
        if(receivedKey == null) {
            return "";
        }

        return receivedKey;
    }

    public static String getReceivedKeyId() {
        if(receivedKeyId == null) {
            return "";
        }

        return receivedKeyId;
    }

    public static void signReceivedKey(String trustLevel) {

    }

    public static void setPublicKey(String pgp_key_id) {
        publicKeyId = pgp_key_id;
        publicKey = GPGCli.getInstance().exportAsciiArmoredKey(publicKeyId);

        Log.i("KeyMaster", publicKey);
    }

    public static String getPublicKey() {
        if(publicKey != null) {
            return publicKey;
        } else {
            return "";
        }
    }

    public static String getPublicKeyId() {
        if(publicKeyId != null) {
            return publicKeyId;
        } else {
            return "";
        }
    }

    public static String getSignedKey() {
        return "This is a signed key";
    }

}
