package org.inigma.shared.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

public abstract class CryptoUtils {
    private static Logger logger = LoggerFactory.getLogger(CryptoUtils.class);

    public static String decrypt(String message, RSAPrivateKeySpec key) {
        return transform(message, Cipher.DECRYPT_MODE, key);
    }

    public static String encrypt(String message, RSAPublicKeySpec key) {
        return transform(message, Cipher.ENCRYPT_MODE, key);
    }

    public static RsaKeyPair generateKeyPair(int bits) throws GeneralSecurityException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(bits);
        KeyPair kp = gen.generateKeyPair();

        RsaKeyPair keyPair = new RsaKeyPair();
        keyPair.setPublicKey(factory.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class));
        keyPair.setPrivateKey(factory.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class));
        return keyPair;
    }

    public static String hashWithSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes());
            return new String(Base64.encode(hash));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Unable to hash contents", e);
        }
    }

    public static String sign(String message, RSAPrivateKeySpec key) {
        return transform(message, Cipher.ENCRYPT_MODE, key);
    }

    public static byte[] transform(byte[] message, int mode, KeySpec key) {
        if (message == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(mode, generateKeyFromSpec(key));
            ByteArrayInputStream bais = new ByteArrayInputStream(message);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (Cipher.DECRYPT_MODE == mode) {
                decode(bais, baos, cipher);
            } else {
                encode(bais, baos, cipher, getBlockSize(key));
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String transform(String message, int mode, KeySpec key) {
        if (message == null) {
            return null;
        }
        return new String(transform(message.getBytes(), mode, key));
    }

    public static String verify(String message, RSAPublicKeySpec key) {
        return transform(message, Cipher.DECRYPT_MODE, key);
    }

    private static void decode(InputStream in, OutputStream out, Cipher cipher) throws IOException {
        StringBuilder message = new StringBuilder();
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = in.read(buffer)) > 0) {
            message.append(new String(buffer, 0, read));
            int indexOf = -1;
            while ((indexOf = message.indexOf(" ")) != -1) {
                byte[] block = null;

                if (indexOf != -1) { // chunk completion
                    block = message.substring(0, indexOf).getBytes();
                    message.replace(0, indexOf + 1, ""); // wipe out the processed portion and leave the rest
                    decodeHelper(block, out, cipher);
                }
            }
        }
        if (message.length() > 0) {
            decodeHelper(message.toString().getBytes(), out, cipher);
        }
    }

    private static void decodeHelper(byte[] block, OutputStream out, Cipher cipher) throws IOException {
        byte[] decode = Base64.decode(block);
        try {
            out.write(cipher.doFinal(decode));
        } catch (Exception e) {
            throw new IOException("Error decrypting data", e);
        }
    }

    private static void encode(InputStream in, OutputStream out, Cipher cipher, int blockSize) throws IOException {
        boolean prepend = false;
        byte[] buffer = new byte[blockSize];
        int read = 0;
        while ((read = in.read(buffer)) > 0) {
            if (prepend) {
                out.write(' '); // block separators
            } else {
                prepend = true;
            }

            try {
                byte[] encrypted = cipher.doFinal(buffer, 0, read);
                byte[] encode = Base64.encode(encrypted);
                out.write(encode);
            } catch (Exception e) {
                throw new IOException("Error encrypting data", e);
            }
        }
    }

    private static Key generateKeyFromSpec(KeySpec spec) {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            if (spec instanceof RSAPublicKeySpec) {
                return factory.generatePublic(spec);
            }
            if (spec instanceof RSAPrivateKeySpec) {
                return factory.generatePrivate(spec);
            }
        } catch (GeneralSecurityException e) {
            logger.error("Error generating key from spec {}", spec, e);
        }
        return null;
    }

    private static int getBlockSize(KeySpec spec) {
        if (spec instanceof RSAPublicKeySpec) {
            return ((RSAPublicKeySpec) spec).getModulus().bitLength() / 8 - 11;
        }
        if (spec instanceof RSAPrivateKeySpec) {
            return ((RSAPrivateKeySpec) spec).getModulus().bitLength() / 8 - 11;
        }
        throw new UnsupportedOperationException("Only RSA Key Specs are supported!");
    }
}
