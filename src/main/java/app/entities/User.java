package app.entities;

import javax.crypto.SecretKey;

public record User(int userId, byte[] hashedPassword, byte[] salt, SecretKey encryptionKey) {
}