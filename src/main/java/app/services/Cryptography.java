package app.services;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import app.exceptions.CryptographyException;

/**
 * Cryptography class for dealing with password hashing and user data encryption<br>
 * uses PBKDF2 for hashing, more precisely "PBKDF2WithHmacSHA512"<br>
 * uses AES256 for symmetric encryption
 */

public class Cryptography {

    private static final int SALT_LENGTH_BYTES = 64;
    private static final int HASH_LENGTH_BITS = 512;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int ITERATION_COUNT = 60000;

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String password, byte[] salt) throws CryptographyException {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, HASH_LENGTH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return hash;
        } catch (Exception e) {
            throw new CryptographyException("Error in hashPassword(): " + e.getMessage());
        }
    }

    // Constant-time comparison method to mitigate timing attacks
    public static boolean compareHashes(byte[] hashA, byte[] hashB) {
        int diff = hashA.length ^ hashB.length;
        for (int i = 0; i < hashA.length && i < hashB.length; i++) {
            diff |= hashA[i] ^ hashB[i];
        }
        return diff == 0;
    }

    public static SecretKey generateKey(String password, byte[] salt) throws CryptographyException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH_BITS);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        } catch (Exception e) {
            throw new CryptographyException("Error in generateKey(): " + e.getMessage());
        }
    }

    public static byte[] encrypt(String strToEncrypt, SecretKey key) throws CryptographyException {

        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);

            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            byte[] encryptedData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

            return encryptedData;
        } catch (Exception e) {
            throw new CryptographyException("Error in encrypt(): " + e.getMessage());
        }

    }

    public static String decrypt(byte[] encryptedData, SecretKey key) throws CryptographyException {

        try {
            byte[] iv = new byte[16];
            System.arraycopy(encryptedData, 0, iv, 0, iv.length);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivspec);

            byte[] cipherText = new byte[encryptedData.length - iv.length];
            System.arraycopy(encryptedData, iv.length, cipherText, 0, cipherText.length);

            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText, "UTF-8");
        } catch (Exception e) {
            throw new CryptographyException("Error in decrypt(): " + e.getMessage());
        }

    }

}