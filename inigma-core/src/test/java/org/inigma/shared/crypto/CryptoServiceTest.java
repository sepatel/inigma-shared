package org.inigma.shared.crypto;

import static org.junit.Assert.*;

import org.inigma.shared.crypto.CryptoService;
import org.inigma.shared.crypto.RsaKeyPair;
import org.junit.Before;
import org.junit.Test;

public class CryptoServiceTest {
    private RsaKeyPair keypair;
    
    @Before
    public void setup() throws Exception {
        keypair = CryptoService.generateKeyPair(512);
    }

    @Test
    public void encryptAndDecryptSimple() {
        String message = "Hello World";
        String encrypt = CryptoService.encrypt(message, keypair.getPublicKey());
        assertNotSame(message, encrypt);
        assertEquals(message, CryptoService.decrypt(encrypt, keypair.getPrivateKey()));
    }

    @Test
    public void encryptAndDecryptMultiBlockMessage() {
        StringBuilder growth = new StringBuilder(512);
        for (int i = 0; i < 512; i++) {
            growth.append((char) (i % 26 + 97));
        }
        String message = growth.toString();
        String encrypt = CryptoService.encrypt(message, keypair.getPublicKey());
        assertNotSame(message, encrypt);
        assertEquals(message, CryptoService.decrypt(encrypt, keypair.getPrivateKey()));
    }

    @Test
    public void encryptAndDecryptNull() {
        String message = null;
        String encrypt = CryptoService.encrypt(message, keypair.getPublicKey());
        assertNull(message, encrypt);
        assertEquals(message, CryptoService.decrypt(encrypt, keypair.getPrivateKey()));
    }

    @Test
    public void signatureAndVerification() {
        String message = "Hello World";
        String encrypt = CryptoService.sign(message, keypair.getPrivateKey());
        assertNotSame(message, encrypt);
        assertEquals(message, CryptoService.verify(encrypt, keypair.getPublicKey()));
    }
    
    @Test
    public void oneWayHashing() {
        String password = "fiddle";
        String hash = CryptoService.hashWithSHA256(password);
        assertNotSame(password, hash);
        assertEquals(hash, CryptoService.hashWithSHA256(password));
        assertEquals("f8LUlo0uaMb47luIM0jcssKzfRnvErsQIxabvKM7Up0=", hash);
    }
}
