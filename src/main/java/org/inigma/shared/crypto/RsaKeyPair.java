package org.inigma.shared.crypto;

import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RsaKeyPair {
    private RSAPrivateKeySpec privateKey;
    private RSAPublicKeySpec publicKey;

    public RSAPrivateKeySpec getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKeySpec getPublicKey() {
        return publicKey;
    }

    public void setPrivateKey(RSAPrivateKeySpec privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(RSAPublicKeySpec publicKey) {
        this.publicKey = publicKey;
    }
}
