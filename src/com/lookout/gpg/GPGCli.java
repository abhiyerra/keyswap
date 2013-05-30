package com.lookout.gpg;

import android.util.Log;

import java.io.*;
import java.util.ArrayList;

public class GPGCli implements GPGBinding {

    private final String GPG_PATH = "/data/data/info.guardianproject.gpg/app_opt/aliases/gpg2";

    public GPGCli() {
        Log.i("LookoutPG", "GPGCli initialized");
    }

    public void ImportKey(String keyFile) {
        Exec(GPG_PATH, "--yes", "--import", keyFile);

        Log.i("LookoutPG", keyFile + " imported");
    }

    public String ExportAsAsciiArmor(String keyId) {
        String output = Exec(GPG_PATH, "--armor", "--export", keyId);
        Log.i("LookoutPG", keyId + " exported");

        return output;
    }

    public ArrayList<GPGKey> GetKeys() {
        String output = Exec(GPG_PATH, "--with-colons", "--list-keys");
        Log.i("LookoutPG", "Got keys: " + output);

        return new ArrayList<GPGKey>();
    }

    private String Exec(String... command) {
        String rawOutput = "";
        try {
            Process p = new ProcessBuilder(command).start();
            p.waitFor();
            rawOutput = getProcessOutput(p);
        } catch(IOException e) {

        } catch (InterruptedException e) {

        }
        return rawOutput;
    }

    private String getProcessOutput(Process p) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            sb.append(line + "\n");
        }
        input.close();

        return sb.toString();
    }
}
