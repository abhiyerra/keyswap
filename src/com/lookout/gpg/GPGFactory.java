package com.lookout.gpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ayerra
 * Date: 5/29/13
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class GPGFactory {
    public static ArrayList<Map<String, String>> keys;

    public static ArrayList<Map<String, String>> getKeys() {
        return keys;
    }

    public static void buildData() {
        keys = new ArrayList<Map<String, String>>();
        keys.add(putData("Abhi Yerra", "EC92C369", "1"));
        keys.add(putData("Shane Wilton", "02C834B6", "2"));
        keys.add(putData("Derek Halliday", "33D8457A", "3"));
    }

    public static HashMap<String, String> putData(String name, String pgp_fingerprint, String trust_level) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("full_name", name);
        item.put("pgp_fingerprint", pgp_fingerprint);
        item.put("trust_level", trust_level);
        return item;
    }

    public static void addKey(String key) {
        keys.add(putData(key, "",""));
    }
}
