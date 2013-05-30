package com.lookout.gpg;

public class GPGRecord {

    public enum Type {
        Public,
        X509,
        X509WithSecret,
        Secret,
        Sub,
        SecretSub,
        UserId,
        UserAttribute,
        Signature,
        RevocationSignature,
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
        Undefined,
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

    private Type type;
    private Validity validity;
    private Algorithm algorithm;
    private int length;
    private String keyId;
    private String creationDate;
    private String expirationDate;
    private String localId;
    private String ownerTrust;
    private String userId;

    public GPGRecord() {
    }

    //A colon-listing is the output of "gpg --with-colons --list-keys"
    public static GPGRecord FromColonListingFactory(String colonListing) {
        GPGRecord key = new GPGRecord();

        String[] fields = colonListing.split(":");
        if(fields.length < 10) {
            return key;
        }

        String type = fields[0];
        if(type.equals("pub")) {
            key.type = Type.Public;
        } else if(type.equals("crt")) {
            key.type = Type.X509;
        } else if(type.equals("crs")) {
            key.type = Type.X509WithSecret;
        } else if(type.equals("sub")) {
            key.type = Type.Sub;
        } else if(type.equals("sec")) {
            key.type = Type.Secret;
        } else if(type.equals("ssb")) {
            key.type = Type.SecretSub;
        } else if(type.equals("uid")) {
            key.type = Type.UserId;
        } else if(type.equals("uat")) {
            key.type = Type.UserAttribute;
        } else if(type.equals("sig")) {
            key.type = Type.Signature;
        } else if(type.equals("rev")) {
            key.type = Type.RevocationSignature;
        } else if(type.equals("fpr")) {
            key.type = Type.Fingerprint;
        } else if(type.equals("pkd")) {
            key.type = Type.PublicKeyData;
        } else if(type.equals("grp")) {
            key.type = Type.KeyGrip;
        } else if(type.equals("rvk")) {
            key.type = Type.RevocationKey;
        } else if(type.equals("tru")) {
            key.type = Type.TrustDatabaseInformation;
        } else if(type.equals("spk")) {
            key.type = Type.SignatureSubpacket;
        }

        //Only the first character of this field determines validity
        char validity = fields[1].charAt(0);
        switch(validity) {
            case 'o':
                key.validity = Validity.New;
                break;
            case 'i':
                key.validity = Validity.Invalid;
                break;
            case 'd':
                key.validity = Validity.Disabled;
                break;
            case 'r':
                key.validity = Validity.Revoked;
                break;
            case 'e':
                key.validity = Validity.Expired;
                break;
            case '-':
                key.validity = Validity.Unknown;
                break;
            case 'q':
                key.validity = Validity.Undefined;
                break;
            case 'm':
                key.validity = Validity.Marginally;
                break;
            case 'f':
                key.validity = Validity.Fully;
                break;
            case 'u':
                key.validity = Validity.Ultimately;
                break;
        }

        try {
            key.length = Integer.parseInt(fields[2]);
        } catch(NumberFormatException e) {
        }

        try {
            int algorithm = Integer.parseInt(fields[3]);
            switch(algorithm) {
                case 1:
                    key.algorithm = Algorithm.RSA;
                    break;
                case 16:
                    key.algorithm = Algorithm.ElgamalEncrypt;
                    break;
                case 17:
                    key.algorithm = Algorithm.DSA;
                    break;
                case 20:
                    key.algorithm = Algorithm.ElgamalSignEncrypt;
                    break;
            }
        } catch(NumberFormatException e) {
        }

        key.keyId          = fields[4];
        key.creationDate   = fields[5];
        key.expirationDate = fields[6];
        key.localId        = fields[7];
        key.ownerTrust     = fields[8];
        key.userId         = fields[9];

        return key;
    }

    public Type getType() {
        return type;
    }

    public Validity getValidity() {
        return validity;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public int getLength() {
        return length;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getLocalId() {
        return localId;
    }

    public String getOwnerTrust() {
        return ownerTrust;
    }

    public String getUserId() {
        return userId;
    }
}
