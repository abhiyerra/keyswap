package com.lookout.gpg;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GPGCli implements GPGBinding {

    private final String GPG_PATH = "/data/data/info.guardianproject.gpg/app_opt/aliases/gpg2";

    public GPGCli() {
        Log.i("LookoutPG", "GPGCli initialized");
    }

    public ArrayList<GPGKey> GetPublicKeys() {
        String rawList = Exec(GPG_PATH, "--with-colons", "--list-keys");
        Log.i("LookoutPG", "Got public keys");

        ArrayList<GPGKey> keys = new ArrayList<GPGKey>();
        Scanner scanner = new Scanner(rawList);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            GPGRecord parentKey = GPGRecord.FromColonListingFactory(line);
            if(parentKey.getType() == GPGRecord.Type.Public) {
                GPGKey key = new GPGKey(parentKey);
                keys.add(key);
                while(scanner.hasNextLine() && !scanner.hasNext(Pattern.compile("pub:.*"))) {
                    GPGRecord subRecord = GPGRecord.FromColonListingFactory(scanner.nextLine());
                    switch(subRecord.getType()) {
                        case UserId:
                            key.AddUserId(subRecord);
                            break;
                        case Sub:
                            key.AddSubKey(subRecord);
                            break;
                    }
                }
            }
        }
        scanner.close();

        return keys;
    }

    public ArrayList<GPGKey> GetSecretKeys() {
        String rawList = Exec(GPG_PATH, "--with-colons", "--list-secret-keys");
        Log.i("LookoutPG", "Got secret keys");

        ArrayList<GPGKey> keys = new ArrayList<GPGKey>();
        Scanner scanner = new Scanner(rawList);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            GPGRecord parentKey = GPGRecord.FromColonListingFactory(line);
            if(parentKey.getType() == GPGRecord.Type.Secret) {
                GPGKey key = new GPGKey(parentKey);
                keys.add(key);
                while(scanner.hasNextLine() && !scanner.hasNext(Pattern.compile("sec:.*"))) {
                    GPGRecord subRecord = GPGRecord.FromColonListingFactory(scanner.nextLine());
                    switch(subRecord.getType()) {
                        case UserId:
                            key.AddUserId(subRecord);
                            break;
                        case SecretSub:
                            key.AddSubKey(subRecord);
                            break;
                    }
                }
            }
        }
        scanner.close();

        return keys;
    }

    public void ExportKey(String destination, String keyId) {
        Exec(GPG_PATH, "--yes", "--output", destination, "--export", keyId);

        Log.i("LookoutPG", keyId + " exported to " + destination);
    }

    public void ImportKey(String source) {
        Exec(GPG_PATH, "--yes", "--import", source);

        Log.i("LookoutPG", source + " imported");
    }

    public String KeyAsAsciiArmor(String keyId) {
        String output = Exec(GPG_PATH, "--armor", "--export", keyId);
        Log.i("LookoutPG", keyId + " exported");

        return output;
    }

    public void PushToKeyServer(String server, String keyId) {
        Exec(GPG_PATH, "--yes", "--key-server", server, "--send-key", keyId);

        Log.i("LookoutPG", keyId + " pushed to " + server);
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
