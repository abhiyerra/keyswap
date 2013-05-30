package com.lookout.gpg;

import java.util.List;

public interface GPGBinding {

    List<GPGKey> GetPublicKeys();
    List<GPGKey> GetSecretKeys();

    void ExportKey(String destination, String keyId);
    void ImportKey(String source);

    void PushToKeyServer(String server, String keyId);

    String KeyAsAsciiArmor(String keyId);
}