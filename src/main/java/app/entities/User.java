package app.entities;

import javax.crypto.SecretKey;

public class User {
    public int userId;
    public byte[] hashedPassword;
    public byte[] salt;
    public SecretKey encryptionKey;
}