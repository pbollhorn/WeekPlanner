package model;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
//import java.io.UnsupportedEncodingException;

/**
 * Cryptography class for dealing with password hashing and user data encryption<br>
 * uses PBKDF2 for hashing, more precisely "PBKDF2WithHmacSHA512"<br>
 * uses AES256 for symmetric encryption
 */
public class Cryptography {

	private static final int SALT_LENGTH_BYTES = 64;
	private static final int HASH_LENGTH_BITS = 512;
	private static final int KEY_LENGTH_BITS = 256;
	private static final int ITERATION_COUNT = 65536;

	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH_BYTES];
		random.nextBytes(salt);
		return salt;
	}

	public static byte[] hashPassword(String password, byte[] salt) {

		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, HASH_LENGTH_BITS);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			return hash;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// I think this is constant-time comparison method to mitigate certain timing attacks
	public static boolean compareHashes(byte[] hashA, byte[] hashB) {

		int diff = hashA.length ^ hashB.length;
		for (int i = 0; i < hashA.length && i < hashB.length; i++) {
			diff |= hashA[i] ^ hashB[i];
		}
		return diff == 0;
	}

	public static SecretKeySpec generateKey(String password, byte[] salt) {

		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH_BITS);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
			return secretKeySpec;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static byte[] encrypt(String strToEncrypt, SecretKeySpec key) {

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
			// Handle the exception properly
			e.printStackTrace();
			return null;
		}
	}

	// Does my function alter encryptedData??? (Bad practice)
	public static String decrypt(byte[] encryptedData, SecretKeySpec key) {

		try {

			byte[] iv = new byte[16];
			System.arraycopy(encryptedData, 0, iv, 0, iv.length);
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, ivspec);

			byte[] cipherText = new byte[encryptedData.length - 16];
			System.arraycopy(encryptedData, 16, cipherText, 0, cipherText.length);

			byte[] decryptedText = cipher.doFinal(cipherText);
			return new String(decryptedText, "UTF-8");
		} catch (Exception e) {
			// Handle the exception properly
			e.printStackTrace();
			return null;
		}
	}

}
