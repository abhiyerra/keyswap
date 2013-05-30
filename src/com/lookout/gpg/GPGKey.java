package com.lookout.gpg;

public class GPGKey {

    public enum KeyType {
        Public,
        X509,
        X509WithSecret,
        Secret,
        Sub,
        SecretSub,
        UserId,
        UserAttribute,
        Signature,
        RevocationCertificate,
        Fingerprint,
        PublicKeyData,
        KeyGrip,
        RevocationKey,
        TrustDatabaseInformation,
        SignatureSubpacket
    }

    public enum Validity {
        New,
        Invalid,
        Disabled,
        Revoked,
        Expired,
        Unknown,
        Valid,
        Marginally,
        Fully,
        Ultimately
    }

    public enum Algorithm {
        RSA,
        ElgamalEncrypt,
        DSA,
        ElgamalSignEncrypt
    }

    public enum Capabilities {
        Encrypt,
        Sign,
        Certify,
        Authentication
    }

    private String keyId;
    private String personalName;
    private String emailAddress;

    public GPGKey(String keyId, String personalName, String emailAddress) {
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
