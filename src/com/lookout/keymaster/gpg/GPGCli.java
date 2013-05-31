package com.lookout.keymaster.gpg;

import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GPGCli implements GPGBinding {

    private static GPGCli instance;

    private final String GPG_PATH = "/data/data/info.guardianproject.gpg/app_opt/aliases/gpg2";

    public static GPGCli getInstance() {
        if(instance == null) {
            instance = new GPGCli();
        }
        return instance;
    }

    private GPGCli() {
        Log.i("LookoutPG", "GPGCli initialized");
    }

    public ArrayList<GPGKey> getPublicKeys() {
        String rawList = Exec(GPG_PATH, "--with-colons", "--with-fingerprint", "--list-keys");
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
                            key.addUserId(subRecord);
                            break;
                        case Sub:
                            key.addSubKey(subRecord);
                            break;
                        case Fingerprint:
                            //Fingerprint records use the userId field as the fingerprint
                            key.setFingerprint(subRecord.getUserId());
                            break;
                    }
                }
            }
        }
        scanner.close();

        return keys;
    }

    public ArrayList<GPGKey> getSecretKeys() {
        String rawList = Exec(GPG_PATH, "--with-colons", "--with-fingerprint", "--list-secret-keys");
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
                            key.addUserId(subRecord);
                            break;
                        case SecretSub:
                            key.addSubKey(subRecord);
                            break;
                        case Fingerprint:
                            //Fingerprint records use the userId field as the fingerprint
                            key.setFingerprint(subRecord.getUserId());
                            break;
                    }
                }
            }
        }
        scanner.close();

        return keys;
    }

    public void signKey(String fingerprint, GPGRecord.TrustLevel trustLevel) {
        int gpgTrustLevel = 1;
        switch(trustLevel) {
            case New:
                gpgTrustLevel = 1;
                break;
            case None:
                gpgTrustLevel = 2;
            case Marginal:
                gpgTrustLevel = 3;
                break;
            case Full:
                gpgTrustLevel = 4;
                break;
            case Ultimate:
                gpgTrustLevel = 5;
                break;
        }

        String trustDBRecord = fingerprint + ":" + gpgTrustLevel + ":";
        try {
            String tempPath = "/sdcard/Keymaster/tempTrustDb";
            PrintWriter printWriter = new PrintWriter(tempPath);
            printWriter.print(trustDBRecord);
            printWriter.close();

            Exec(GPG_PATH, "--import-ownertrust", tempPath);
            new File(tempPath).delete();
        } catch(Exception e) {
        }

    }

    public void exportKeyring(String destination) {
        Exec(GPG_PATH, "--yes", "--output", destination, "--export");

        Log.i("LookoutPG", "Keyring exported");
    }

    public void exportKey(String destination, String keyId) {
        String outputPath = new File(destination, keyId + ".gpg").getAbsolutePath();
        Exec(GPG_PATH, "--yes", "--output", outputPath, "--export", keyId);

        Log.i("LookoutPG", keyId + " exported to " + outputPath);
    }

    public void importKey(String source) {
        Exec(GPG_PATH, "--yes", "--import", source);

        Log.i("LookoutPG", source + " imported");
    }

    public String keyAsAsciiArmor(String keyId) {
        String output = Exec(GPG_PATH, "--armor", "--export", keyId);
        Log.i("LookoutPG", keyId + " exported");

        return output;
    }

    public void pushToKeyServer(String server, String keyId) {
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
