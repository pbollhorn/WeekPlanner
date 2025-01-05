package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.crypto.SecretKey;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import app.entities.User;
import app.entities.Credentials;
import app.services.Cryptography;

public class Database {

//    public static DataSource connectionPool;

//    public static void init() {
//
//        try {
//            connectionPool = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/WeekPlannerDB");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//    }

    // Return 1 on success, return 0 on failure
    public static int saveData(User user, String jsonString, ConnectionPool connectionPool) {

        try {
            // Establish connection
            Connection connection = connectionPool.getConnection();
            System.out.println("Connected to PostgreSQL");

            byte[] encryptedData = Cryptography.encrypt(jsonString, user.encryptionKey);

            // Update user_data via prepared statement
            PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET encrypted_data=? WHERE user_id=? AND password_hash=?");
            statement.setBytes(1, encryptedData);
            statement.setInt(2, user.userId);
            statement.setBytes(3, user.hashedPassword);
            int rowsAffected = statement.executeUpdate();

            // Close connection
            statement.close();
            connection.close();

            return rowsAffected;

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
            return 0;
        }

    }

    // Return jsonString on success, return null on failure
    public static String loadData(User user, ConnectionPool connectionPool) {

        String jsonString = null;

        try {

            // Establish connection
            Connection connection = connectionPool.getConnection();
            System.out.println("Connected to PostgreSQL");

            // Get encryptedData from user_data via prepared statement
            PreparedStatement statement = connection.prepareStatement("SELECT encrypted_data FROM user_data WHERE user_id=? AND password_hash=?");
            statement.setInt(1, user.userId);
            statement.setBytes(2, user.hashedPassword);
            ResultSet result = statement.executeQuery();

            byte[] encryptedData = null;
            while (result.next()) {
                encryptedData = result.getBytes(1);
            }

            // Decrypt the data
            jsonString = Cryptography.decrypt(encryptedData, user.encryptionKey);

            // Close connection
            statement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
        }

        return jsonString;

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
            String jsonString = Database.loadData(user, connectionPool);

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
