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

    private KeyType type;
    private Validity validity;
    private Algorithm algorithm;
    private int length;
    private String keyId;
    private String creationDate;
    private String expirationDate;
    private String localId;
    private String ownerTrust;
    private String userId;

    public GPGKey() {
    }

    //A colon-listing is the output of "gpg --with-colons --list-keys"
    public static GPGKey FromColonListingFactory(String colonListing) {
        GPGKey key = new GPGKey();

        String[] fields = colonListing.split(":");
        if(fields.length < 10) {
            return null;
        }

        String type = fields[0];
        if(type.equals("pub")) {
            key.type = KeyType.Public;
        } else if(type.equals("crt")) {
            key.type = KeyType.X509;
        } else if(type.equals("crs")) {
            key.type = KeyType.X509WithSecret;
        } else if(type.equals("sub")) {
            key.type = KeyType.Sub;
        } else if(type.equals("sec")) {
            key.type = KeyType.Secret;
        } else if(type.equals("ssb")) {
            key.type = KeyType.SecretSub;
        } else if(type.equals("uid")) {
            key.type = KeyType.UserId;
        } else if(type.equals("uat")) {
            key.type = KeyType.UserAttribute;
        } else if(type.equals("sig")) {
            key.type = KeyType.Signature;
        } else if(type.equals("rev")) {
            key.type = KeyType.RevocationSignature;
        } else if(type.equals("fpr")) {
            key.type = KeyType.Fingerprint;
        } else if(type.equals("pkd")) {
            key.type = KeyType.PublicKeyData;
        } else if(type.equals("grp")) {
            key.type = KeyType.KeyGrip;
        } else if(type.equals("rvk")) {
            key.type = KeyType.RevocationKey;
        } else if(type.equals("tru")) {
            key.type = KeyType.TrustDatabaseInformation;
        } else if(type.equals("spk")) {
            key.type = KeyType.SignatureSubpacket;
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
}
