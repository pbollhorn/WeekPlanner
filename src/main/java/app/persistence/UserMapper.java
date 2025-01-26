package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.crypto.SecretKey;

import app.entities.User;
import app.entities.Credentials;
import app.exceptions.MapperException;
import app.services.Cryptography;

public class UserMapper {

    /**
     * Attempt to login.
     *
     * @param credentials Username and password
     * @return User object on success, null on failure
     */
    public static User login(Credentials credentials, ConnectionPool connectionPool) throws MapperException {

        String sql = "SELECT user_id, password_hash, salt FROM user_data WHERE username=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, credentials.username());

            ResultSet result = ps.executeQuery();
            if (result.next()) {
                int userId = result.getInt(1);
                byte[] hashedPasswordFromDB = result.getBytes(2);
                byte[] salt = result.getBytes(3);

                // Compare password hashes, return null if they don't match
                byte[] hashedPasswordFromUser = Cryptography.hashPassword(credentials.password(), salt);
                if (Cryptography.compareHashes(hashedPasswordFromUser, hashedPasswordFromDB) == false) {
                    return null;
                }

                // Return User object
                SecretKey encryptionKey = Cryptography.generateKey(credentials.password(), salt);
                return new User(userId, hashedPasswordFromDB, salt, encryptionKey);
            }

            return null;

        } catch (Exception e) {
            throw new MapperException("Error in UserMapper.login(): " + e.getMessage());
        }

    }

    public static void createUser(Credentials credentials, ConnectionPool connectionPool) throws MapperException {

        String sql = "INSERT INTO user_data (username, password_hash, salt, encrypted_data) VALUES (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            byte[] salt = Cryptography.generateSalt();
            byte[] hashedPassword = Cryptography.hashPassword(credentials.password(), salt);
            SecretKey encryptionKey = Cryptography.generateKey(credentials.password(), salt);

            String data = """
                    {"lists":[{"name":"Monday","tasks":[{"description":"Create Week Planner account","doneStatus":true},{"description":"Fill in some tasks","doneStatus":false}]},{"name":"Tuesday","tasks":[]},{"name":"Wednesday","tasks":[]},{"name":"Thursday","tasks":[]},{"name":"Friday","tasks":[]},{"name":"Saturday","tasks":[]},{"name":"Sunday","tasks":[]},{"name":"Next Week","tasks":[]},{"name":"Within a Month","tasks":[]},{"name":"Within a Year","tasks":[]},{"name":"Shopping","tasks":[]}]}""";
            byte[] encryptedData = Cryptography.encrypt(data, encryptionKey);

            ps.setString(1, credentials.username());
            ps.setBytes(2, hashedPassword);
            ps.setBytes(3, salt);
            ps.setBytes(4, encryptedData);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new Exception(rowsAffected + " rows written to database");
            }

        } catch (Exception e) {
            throw new MapperException("Error in UserMapper.createUser(): " + e.getMessage());
        }

    }

//    public static void deleteUser(User user, ConnectionPool connectionPool) throws DatabaseException {
//
//        String sql = "DELETE FROM user_data WHERE user_id = ?";
//
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//
//            ps.setInt(1, user.userId());
//
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected != 1) {
//                throw new DatabaseException("Error deleting user: " + rowsAffected + " rows deleted from database");
//            }
//
//        } catch (Exception e) {
//            throw new DatabaseException("Error deleting user: " + e.getMessage());
//        }
//
//    }

//    public static int changeUsername(User user, String newUsername, ConnectionPool connectionPool) {
//
//        try {
//
//            // Establish connection
//            Connection connection = connectionPool.getConnection();
//
//            // Execute prepared statement
//            PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET username=? WHERE user_id=?");
//            statement.setString(1, newUsername);
//            statement.setInt(2, user.userId());
//            int rowsAffected = statement.executeUpdate();
//
//            // Close connection
//            statement.close();
//            connection.close();
//
//            if (rowsAffected != 1) {
//                return -1;
//            }
//
//            return 1;
//
//        } catch (Exception e) {
//            System.out.println("Error in connecting to PostgreSQL server");
//            e.printStackTrace();
//
//            return -1;
//        }
//
//    }

//    public static int changePassword(User user, String newPassword, ConnectionPool connectionPool) {
//
//        try {
//
//            // Establish connection
//            Connection connection = connectionPool.getConnection();
//
//            // Get unencrypted userdata
//            String jsonString = DataMapper.loadData(user, connectionPool);
//
//            // Salt and hash new password and create new encryption key
//            byte[] hashedPassword = Cryptography.hashPassword(newPassword, user.salt());
//            SecretKey encryptionKey = Cryptography.generateKey(newPassword, user.salt());
//
//            // Encrypt user data with new encryption key
//            byte[] encryptedData = Cryptography.encrypt(jsonString, encryptionKey);
//
//            // Execute prepared statement
//            PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET password_hash=?, encrypted_data=? WHERE user_id=?");
//            statement.setBytes(1, hashedPassword);
//            statement.setBytes(2, encryptedData);
//            statement.setInt(3, user.userId());
//            int rowsAffected = statement.executeUpdate();
//
//            // Close connection
//            statement.close();
//            connection.close();
//
//            if (rowsAffected != 1) {
//                return -1;
//            }
//
////            TODO: Fix this beacuse User is now a record
////            user.hashedPassword = hashedPassword;
////            user.encryptionKey = encryptionKey;
//            return 1;
//
//        } catch (Exception e) {
//            System.out.println("Error in connecting to PostgreSQL server");
//            e.printStackTrace();
//
//            return -1;
//        }
//    }

}