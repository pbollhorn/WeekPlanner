package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import controllers.Credentials;

public class Database {

	public static DataSource connectionPool;

	public static void init() {

		try {
			connectionPool = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/WeekPlannerDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static void createTables() {

		try {
			// Establish connection
			Connection connection = connectionPool.getConnection();
			System.out.println("Connected to PostgreSQL");

			// Create table user_data
			Statement statement = connection.createStatement();
			String sql = "CREATE TABLE user_data(user_id SERIAL PRIMARY KEY, username TEXT, password_hash BYTEA, salt BYTEA, encrypted_data BYTEA)";
			System.out.println(sql);
			statement.execute(sql);

			// Close connection
			statement.close();
			connection.close();

		} catch (Exception e) {
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}

	}

	// Return 1 on success, return 0 on failure
	public static int saveDataNEW(Credentials credentials, String jsonString) {

		try {
			// Establish connection
			Connection connection = connectionPool.getConnection();
			System.out.println("Connected to PostgreSQL");

			byte[] encryptedData = Cryptography.encrypt(jsonString, credentials.encryptionKey);

			// Update user_data via prepared statement
			PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET encrypted_data=? WHERE username=? AND password_hash=?");
			// statement.setObject(1, jsonString, java.sql.Types.OTHER);
			statement.setBytes(1, encryptedData);
			statement.setString(2, credentials.username);
			statement.setBytes(3, credentials.hashedPassword);
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
	public static String loadDataNEW(Credentials credentials) {

		String jsonString = null;

		try {

			// Establish connection
			Connection connection = connectionPool.getConnection();
			System.out.println("Connected to PostgreSQL");

			// Get encryptedData from user_data via prepared statement
			PreparedStatement statement = connection.prepareStatement("SELECT encrypted_data FROM user_data WHERE username=? AND password_hash=?");
			statement.setString(1, credentials.username);
			statement.setBytes(2, credentials.hashedPassword);
			ResultSet result = statement.executeQuery();

			byte[] encryptedData = null;
			while (result.next()) {
				encryptedData = result.getBytes(1);
			}

			// Decrypt the data
			jsonString = Cryptography.decrypt(encryptedData, credentials.encryptionKey);

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
	 * Attempt to login. Store hashedPassword in credentials if success.
	 * 
	 * @param credentials Username and password
	 * @return true for success, false for failure
	 */
	public static boolean login(Credentials credentials) {

		try {

			// Establish connection
			Connection connection = connectionPool.getConnection();

			// Execute prepared statement
			PreparedStatement statement = connection.prepareStatement("SELECT password_hash, salt FROM user_data WHERE username=?");
			statement.setString(1, credentials.username);
			ResultSet result = statement.executeQuery();

			byte[] hashedPasswordFromDB;
			byte[] salt;
			if (result.next()) {
				hashedPasswordFromDB = result.getBytes(1);
				salt = result.getBytes(2);
			} else {
				// Close connection
				statement.close();
				connection.close();
				return false;
			}

			// Close connection
			statement.close();
			connection.close();

			// Compare password hashes
			byte[] hashedPasswordFromUser = Cryptography.hashPassword(credentials.password, salt);
			if (Cryptography.compareHashes(hashedPasswordFromUser, hashedPasswordFromDB) == false) {
				return false;
			}

			// Save hashedPassword to credentials
			credentials.hashedPassword = hashedPasswordFromDB;

			// Generate key and store in credentials
			credentials.encryptionKey = Cryptography.generateKey(credentials.password, salt);

			return true;

		} catch (Exception e) {
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();

			return false;
		}

	}

	// Return true if username is present in database, otherwise failure
	// HAS TO BE CODED STRONGER, BECAUSE IF AN EXCEPTION OCCURS IT DOES NOT MEAN FALSE (THAT THE USER IS NOT IN THE DATABASE)
	public static boolean checkUsername(Credentials credentials) {

		try {

			// Establish connection
			Connection connection = connectionPool.getConnection();

			// Execute prepared statement
			PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM user_data WHERE username=?");
			statement.setObject(1, credentials.username);
			ResultSet result = statement.executeQuery();

			boolean status = false;
			if (result.next()) {
				status = true;
			}

			// Close connection
			statement.close();
			connection.close();

			return status;

		} catch (Exception e) {
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();

			return false;
		}

	}

	public static int createUser(Credentials credentials) {

		String jsonString = "{\"lists\":[{\"name\":\"Monday\",\"tasks\":[{\"description\":\"Dette er din test bruger\",\"done\":false}]},{\"name\":\"Tuesday\",\"tasks\":[]},{\"name\":\"Wednesday\",\"tasks\":[]},{\"name\":\"Thursday\",\"tasks\":[]},{\"name\":\"Friday\",\"tasks\":[]},{\"name\":\"Saturday\",\"tasks\":[]},{\"name\":\"Sunday\",\"tasks\":[]},{\"name\":\"Next Week\",\"tasks\":[]},{\"name\":\"Within a Month\",\"tasks\":[]},{\"name\":\"Within a Year\",\"tasks\":[]}]}";

		try {

			// Establish connection
			Connection connection = connectionPool.getConnection();

			// Generate salt, hash password, generate key
			byte[] salt = Cryptography.generateSalt();
			credentials.hashedPassword = Cryptography.hashPassword(credentials.password, salt);
			credentials.encryptionKey = Cryptography.generateKey(credentials.password, salt);

			// Encrypt user data
			byte[] encryptedData = Cryptography.encrypt(jsonString, credentials.encryptionKey);

			// Execute prepared statement
			PreparedStatement statement = connection.prepareStatement("INSERT INTO user_data (username, password_hash, salt, encrypted_data) VALUES (?,?,?,?)");
			statement.setString(1, credentials.username);
			statement.setBytes(2, credentials.hashedPassword);
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

	public static void main(String[] args) {
//		init();
//		createTables();
	}

}
