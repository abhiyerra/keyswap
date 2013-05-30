package com.lookout.gpg;

import java.io.IOException;
import java.util.List;

public interface GPGBinding {

    void ImportKey(String keyFile) throws IOException;
    String ExportAsAsciiArmor(PublicKey key) throws IOException;
    List<PublicKey> GetKeys() throws IOException;
}
