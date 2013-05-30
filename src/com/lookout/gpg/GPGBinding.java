package com.lookout.gpg;

import java.util.List;

public interface GPGBinding {

    void ImportKey(String keyFile);
    String ExportAsAsciiArmor(String keyId);
    List<GPGKey> GetKeys();
}
