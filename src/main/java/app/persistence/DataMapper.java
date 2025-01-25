package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.SecretKey;

import app.entities.User;
import app.entities.Credentials;
import app.exceptions.DatabaseException;
import app.services.Cryptography;

public class DataMapper {

    public static void saveData(User user, String data, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "UPDATE user_data SET encrypted_data=? WHERE user_id=? AND password_hash=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            byte[] encryptedData = Cryptography.encrypt(data, user.encryptionKey);

            ps.setBytes(1, encryptedData);
            ps.setInt(2, user.userId);
            ps.setBytes(3, user.hashedPassword);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Error saving data to database: Nothing written to database");
            }
            if (rowsAffected > 1) {
                throw new DatabaseException("Error saving data to database: More than one row affected!!!");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error saving data to database: " + e.getMessage());
        }

    }

    public static String loadData(User user, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT encrypted_data FROM user_data WHERE user_id=? AND password_hash=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user.userId);
            ps.setBytes(2, user.hashedPassword);

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                byte[] encryptedData = result.getBytes(1);
                String data = Cryptography.decrypt(encryptedData, user.encryptionKey);
                return data;
            }

            throw new DatabaseException("Error loading data from database: Nothing read from database");

        } catch (SQLException e) {
            throw new DatabaseException("Error loading data from database: " + e.getMessage());
        }

    }

    /**
     * Attempt to login.
     *
     * @param credentials Username and password
     * @return User object on success, null on failure
     */
    public static User login(Credentials credentials, ConnectionPool connectionPool) {

        try {

            // Establish connection
            Connection connection = connectionPool.getConnection();

            // Get hashed password and salt from DB
            PreparedStatement statement = connection.prepareStatement("SELECT user_id, password_hash, salt FROM user_data WHERE username=?");
            statement.setString(1, credentials.username);
            ResultSet result = statement.executeQuery();

            int userId;
            byte[] hashedPasswordFromDB;
            byte[] salt;
            if (result.next()) {
                userId = result.getInt(1);
                hashedPasswordFromDB = result.getBytes(2);
                salt = result.getBytes(3);
            } else {
                // Close connection
                statement.close();
                connection.close();
                return null;
            }

            // Close connection
            statement.close();
            connection.close();

            // Compare password hashes
            byte[] hashedPasswordFromUser = Cryptography.hashPassword(credentials.password, salt);
            if (Cryptography.compareHashes(hashedPasswordFromUser, hashedPasswordFromDB) == false) {
                return null;
            }

            // Save hashedPassword and salt to user
            // and generate key and also store in user
            User user = new User();
            user.userId = userId;
            user.hashedPassword = hashedPasswordFromDB;
            user.salt = salt;
            user.encryptionKey = Cryptography.generateKey(credentials.password, salt);

            return user;

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();

            return null;
        }

    }

    // Return 1 if username is available, 0 if taken, -1 if there was an error
    public static int usernameAvailable(Credentials credentials, ConnectionPool connectionPool) {

        try {

            // Establish connection
            Connection connection = connectionPool.getConnection();

            // Execute prepared statement
            PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM user_data WHERE username=?");
            statement.setObject(1, credentials.username);
            ResultSet result = statement.executeQuery();

            int available = 1;
            if (result.next()) {
                available = 0;
            }

            // Close connection
            statement.close();
            connection.close();

            return available;

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();

            return -1;
        }

    }

    public static int deleteUser(User user, ConnectionPool connectionPool) {

        try {

            Connection connection = connectionPool.getConnection();

            PreparedStatement statement = connection.prepareStatement("DELETE FROM user_data WHERE user_id = ?");
            statement.setInt(1, user.userId);
            int rowsAffected = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsAffected;

        } catch (Exception e) {

            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();

            return 0;
        }

    }

    public static int changeUsername(User user, String newUsername, ConnectionPool connectionPool) {

        try {

            // Establish connection
            Connection connection = connectionPool.getConnection();

            // Execute prepared statement
            PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET username=? WHERE user_id=?");
            statement.setString(1, newUsername);
            statement.setInt(2, user.userId);
            int rowsAffected = statement.executeUpdate();

            // Close connection
            statement.close();
            connection.close();

            if (rowsAffected != 1) {
                return -1;
            }

            return 1;

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();

            return -1;
        }

    }

    public static int changePassword(User user, String newPassword, ConnectionPool connectionPool) {

        try {

            // Establish connection
            Connection connection = connectionPool.getConnection();

            // Get unencrypted userdata
            String jsonString = DataMapper.loadData(user, connectionPool);

            // Salt and hash new password and create new encryption key
            byte[] hashedPassword = Cryptography.hashPassword(newPassword, user.salt);
            SecretKey encryptionKey = Cryptography.generateKey(newPassword, user.salt);

            // Encrypt user data with new encryption key
            byte[] encryptedData = Cryptography.encrypt(jsonString, encryptionKey);

            // Execute prepared statement
            PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET password_hash=?, encrypted_data=? WHERE user_id=?");
            statement.setBytes(1, hashedPassword);
            statement.setBytes(2, encryptedData);
            statement.setInt(3, user.userId);
            int rowsAffected = statement.executeUpdate();

            // Close connection
            statement.close();
            connection.close();

            if (rowsAffected != 1) {
                return -1;
            }

            user.hashedPassword = hashedPassword;
            user.encryptionKey = encryptionKey;
            return 1;

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();

            return -1;
        }
    }

    public static int createUser(Credentials credentials, ConnectionPool connectionPool) {

        String jsonString = "{\"lists\":[{\"name\":\"Monday\",\"tasks\":[{\"description\":\"Dette er din test bruger\",\"done\":false}]},{\"name\":\"Tuesday\",\"tasks\":[]},{\"name\":\"Wednesday\",\"tasks\":[]},{\"name\":\"Thursday\",\"tasks\":[]},{\"name\":\"Friday\",\"tasks\":[]},{\"name\":\"Saturday\",\"tasks\":[]},{\"name\":\"Sunday\",\"tasks\":[]},{\"name\":\"Next Week\",\"tasks\":[]},{\"name\":\"Within a Month\",\"tasks\":[]},{\"name\":\"Within a Year\",\"tasks\":[]}]}";

        try {

            // Establish connection
            Connection connection = connectionPool.getConnection();

            // Generate salt, hashed password and encryption key
            byte[] salt = Cryptography.generateSalt();
            byte[] hashedPassword = Cryptography.hashPassword(credentials.password, salt);
            SecretKey encryptionKey = Cryptography.generateKey(credentials.password, salt);

            // Encrypt user data
            byte[] encryptedData = Cryptography.encrypt(jsonString, encryptionKey);

            // Execute prepared statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user_data (username, password_hash, salt, encrypted_data) VALUES (?,?,?,?)");
            statement.setString(1, credentials.username);
            statement.setBytes(2, hashedPassword);
            statement.setBytes(3, salt);
            statement.setBytes(4, encryptedData);
            int rowsAffected = statement.executeUpdate();

            // Close connection
            statement.close();
            connection.close();

            return rowsAffected;

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();

            return -1;
        }

    }

}
