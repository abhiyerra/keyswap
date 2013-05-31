package com.lookout.keymaster.gpg;

import android.content.Context;

import java.util.List;

public interface GPGBinding {

    List<GPGKey> getPublicKeys();
    List<GPGKey> getSecretKeys();

    void exportKeyring(String destination);
    void exportKey(String destination, String keyId);
    void importKey(String source);

    void signKey(String fingerprint, GPGRecord.TrustLevel trustLevel);

    void pushToKeyServer(String server, String keyId);

    String keyAsAsciiArmor(String keyId);
}