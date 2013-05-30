package com.lookout.keymaster;

import android.content.Context;

import java.util.List;

public interface GPGBinding {

    List<GPGKey> getPublicKeys();
    List<GPGKey> getSecretKeys();

    void exportKey(String destination, String keyId);
    void importKey(String source);

    void pushToKeyServer(String server, String keyId);

    String keyAsAsciiArmor(String keyId);
}