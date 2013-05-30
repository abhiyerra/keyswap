package com.lookout.gpg;

public class PublicKey {
    private String keyId;
    private String personalName;
    private String emailAddress;

    public PublicKey(String keyId, String personalName, String emailAddress) {
        this.keyId        = keyId;
        this.personalName = personalName;
        this.emailAddress = emailAddress;
    }

    public String GetKeyId() {
        return this.keyId;
    }

    public String GetPersonalName() {
        return this.personalName;
    }

    public String GetEmailAddress() {
        return this.emailAddress;
    }
}
