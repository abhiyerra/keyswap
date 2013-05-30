package com.lookout.gpg;

import java.util.ArrayList;

public class GPGKey {
    private GPGRecord parentKey;
    private ArrayList<GPGRecord> userIds;
    private ArrayList<GPGRecord> subKeys;

    public GPGKey(GPGRecord parentKey) {
        this.parentKey = parentKey;

        this.userIds = new ArrayList<GPGRecord>();
        this.subKeys = new ArrayList<GPGRecord>();
    }

    public String GetKeyId() {
        return parentKey.getKeyId();
    }

    public GPGRecord GetPrimaryUserId() {
        if(userIds.isEmpty()) {
            return null;
        }

        return userIds.get(0);
    }

    public void AddUserId(GPGRecord userId) {
        userIds.add(userId);
    }

    public void AddSubKey(GPGRecord subKey) {
        subKeys.add(subKey);
    }

    public GPGRecord.Type GetType() {
        return parentKey.getType();
    }

    public ArrayList<GPGRecord> GetUserIds() {
        return userIds;
    }

    public ArrayList<GPGRecord> GetSubKeys() {
        return subKeys;
    }
}
