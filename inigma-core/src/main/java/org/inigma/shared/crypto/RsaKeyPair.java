package org.inigma.shared.crypto;

import java.math.BigInteger;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RsaKeyPair {
    private static class PrivateKeySpec {
        private BigInteger modulus;
        private BigInteger privateExponent;
    }

    private static class PublicKeySpec {
        private BigInteger modulus;
        private BigInteger publicExponent;
    }

    private PrivateKeySpec privateKey;
    private PublicKeySpec publicKey;

    public RSAPrivateKeySpec getPrivateKey() {
        if (privateKey == null) {
            return null;
        }
        return new RSAPrivateKeySpec(privateKey.modulus, privateKey.privateExponent);
    }

    public void setPrivateKey(RSAPrivateKeySpec privateKey) {
        if (privateKey == null) {
            this.privateKey = null;
        } else {
            this.privateKey = new PrivateKeySpec();
            this.privateKey.modulus = privateKey.getModulus();
            this.privateKey.privateExponent = privateKey.getPrivateExponent();
        }
    }

    public RSAPublicKeySpec getPublicKey() {
        if (publicKey == null) {
            return null;
        }
        return new RSAPublicKeySpec(publicKey.modulus, publicKey.publicExponent);
    }

    public void setPublicKey(RSAPublicKeySpec publicKey) {
        if (publicKey == null) {
            this.publicKey = null;
        } else {
            this.publicKey = new PublicKeySpec();
            this.publicKey.modulus = publicKey.getModulus();
            this.publicKey.publicExponent = publicKey.getPublicExponent();
        }
    }
}
