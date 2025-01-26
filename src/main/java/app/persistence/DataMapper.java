package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.services.Cryptography;

public class DataMapper {

    public static String loadData(User user, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT encrypted_data FROM user_data WHERE user_id=? AND password_hash=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user.userId());
            ps.setBytes(2, user.hashedPassword());

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                byte[] encryptedData = result.getBytes(1);
                String data = Cryptography.decrypt(encryptedData, user.encryptionKey());
                return data;
            }

            throw new DatabaseException("Error loading data from database: Nothing read from database");

        } catch (Exception e) {
            throw new DatabaseException("Error loading data from database: " + e.getMessage());
        }

    }

    public static void saveData(User user, String data, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "UPDATE user_data SET encrypted_data=? WHERE user_id=? AND password_hash=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            byte[] encryptedData = Cryptography.encrypt(data, user.encryptionKey());

            System.out.println(encryptedData.length);

            ps.setBytes(1, encryptedData);
            ps.setInt(2, user.userId());
            ps.setBytes(3, user.hashedPassword());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error saving data to database: " + rowsAffected + " rows written to database");
            }

        } catch (Exception e) {
            throw new DatabaseException("Error saving data to database: " + e.getMessage());
        }

    }

}