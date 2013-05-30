package com.lookout.gpg;

import java.io.*;
import java.util.ArrayList;

public class GPGCli implements GPGBinding {

    private final String GPG_PATH = "/data/data/info.guardianproject.gpg/app_opt/aliases/gpg2";

    public GPGCli() throws IOException {
        String[] arguments = {"--version"};
        Runtime.getRuntime().exec(GPG_PATH, arguments);
    }

    public void ImportKey(String keyFile) throws IOException {
        String[] arguments = {"--import", keyFile};
        Runtime.getRuntime().exec(GPG_PATH, arguments);
    }

    public String ExportAsAsciiArmor(PublicKey key) throws IOException {
        String[] arguments = {"--armor", "--export", key.GetKeyId()};
        Process p = Runtime.getRuntime().exec(GPG_PATH, arguments);

        return getProcessOutput(p);
    }

    public ArrayList<PublicKey> GetKeys() throws IOException {
        String[] arguments = {"--list-keys"};
        Process p = Runtime.getRuntime().exec(GPG_PATH, arguments);

        throw new IOException();
    }

    private String getProcessOutput(Process p) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder s = new StringBuilder();
        String line;
        while((line = bf.readLine()) != null) {
            s.append(line);
        }

        return s.toString();
    }
}
