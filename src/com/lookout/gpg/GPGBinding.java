package com.lookout.gpg;

import java.util.List;

public interface GPGBinding {

    List<GPGKey> GetKeys();

    void ExportKey(String destination, String keyId);
    void ImportKey(String source);

    void PushToKeyServer(String server, String keyId);

    String KeyAsAsciiArmor(String keyId);
}