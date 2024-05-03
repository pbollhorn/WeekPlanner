package controllers;

import javax.crypto.spec.SecretKeySpec;

public class Credentials {

	// TODO: Potentielt sikkerhedshul fordi API bruger kan sende hashedPassword med JSON
	public String username;
	public String password;
	public byte[] hashedPassword;
	public SecretKeySpec encryptionKey;

	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
