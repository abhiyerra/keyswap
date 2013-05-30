package com.lookout.keymaster.gpg;

import java.util.ArrayList;

public class GPGKey {
    private GPGRecord parentKey;
    private String fingerprint;
    private ArrayList<GPGRecord> userIds;
    private ArrayList<GPGRecord> subKeys;

    public GPGKey(GPGRecord parentKey) {
        this.parentKey = parentKey;

        this.userIds = new ArrayList<GPGRecord>();
        this.subKeys = new ArrayList<GPGRecord>();
    }

    public String getKeyId() {
        return parentKey.getKeyId();
    }

    public GPGRecord getPrimaryKeyId() {
        if(userIds.isEmpty()) {
            return null;
        }

        return userIds.get(0);
    }

    public void addUserId(GPGRecord userId) {
        userIds.add(userId);
    }

    public void addSubKey(GPGRecord subKey) {
        subKeys.add(subKey);
    }

    public GPGRecord.Type getType() {
        return parentKey.getType();
    }

    public GPGRecord getParentKey() {
        return parentKey;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public ArrayList<GPGRecord> getUserIds() {
        return userIds;
    }

    public ArrayList<GPGRecord> getSubKeys() {
        return subKeys;
    }
}
