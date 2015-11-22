package org.inigma.shared.crypto;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CryptoUtilsTest {
    private RsaKeyPair keypair;

    @Before
    public void setup() throws Exception {
        keypair = CryptoUtils.generateKeyPair(512);
    }

    @Test
    public void encryptAndDecryptSimple() {
        String message = "Hello World";
        String encrypt = CryptoUtils.encrypt(message, keypair.getPublicKey());
        assertNotSame(message, encrypt);
        assertEquals(message, CryptoUtils.decrypt(encrypt, keypair.getPrivateKey()));
    }

    @Test
    public void encryptAndDecryptMultiBlockMessage() {
        StringBuilder growth = new StringBuilder(512);
        for (int i = 0; i < 512; i++) {
            growth.append((char) (i % 26 + 97));
        }
        String message = growth.toString();
        String encrypt = CryptoUtils.encrypt(message, keypair.getPublicKey());
        assertNotSame(message, encrypt);
        assertEquals(message, CryptoUtils.decrypt(encrypt, keypair.getPrivateKey()));
    }

    @Test
    public void encryptAndDecryptNull() {
        String message = null;
        String encrypt = CryptoUtils.encrypt(message, keypair.getPublicKey());
        assertNull(message, encrypt);
        assertEquals(message, CryptoUtils.decrypt(encrypt, keypair.getPrivateKey()));
    }

    @Test
    public void signatureAndVerification() {
        String message = "Hello World";
        String encrypt = CryptoUtils.sign(message, keypair.getPrivateKey());
        assertNotSame(message, encrypt);
        assertEquals(message, CryptoUtils.verify(encrypt, keypair.getPublicKey()));
    }

    @Test
    public void oneWayHashing() {
        String password = "fiddle";
        String hash = CryptoUtils.hashWithSHA256(password);
        assertNotSame(password, hash);
        assertEquals(hash, CryptoUtils.hashWithSHA256(password));
        assertEquals("f8LUlo0uaMb47luIM0jcssKzfRnvErsQIxabvKM7Up0=", hash);
    }
}
