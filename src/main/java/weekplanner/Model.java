package weekplanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Model {

	public static DataSource connectionPool;

	public static void init() {

		try {
			connectionPool = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/WeekPlannerDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static void databaseCreateTables() {

		try {
			// Establish connection
			Connection connection = connectionPool.getConnection();
			System.out.println("Connected to PostgreSQL");

			// Create table user_data
			Statement statement = connection.createStatement();
			String sql = "CREATE TABLE user_data(user_id SERIAL PRIMARY KEY, username TEXT, password TEXT, plan JSON)";
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

	// Return 1 on success, return 0 or -1 on failure
	public static int saveData(Credentials credentials, String jsonString) {

		try {
			// Establish connection
			Connection connection = connectionPool.getConnection();
			System.out.println("Connected to PostgreSQL");

			// Update user_data via prepared statement
			PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET plan=? WHERE username=? AND password=?");
			statement.setObject(1, jsonString, java.sql.Types.OTHER);
			statement.setObject(2, credentials.username);
			statement.setObject(3, credentials.password);
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

	// Return jsonString on success, return null on failure
	public static String loadData(Credentials credentials) {

		String jsonString = null;

		try {

			// Establish connection
			Connection connection = connectionPool.getConnection();
			System.out.println("Connected to PostgreSQL");

			// Get plan from user_data via prepared statement
			PreparedStatement statement = connection.prepareStatement("SELECT plan FROM user_data WHERE username=? AND password=?");
			statement.setObject(1, credentials.username);
			statement.setObject(2, credentials.password);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				jsonString = result.getString("plan");
			}

			// Close connection
			statement.close();
			connection.close();

		} catch (Exception e) {
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}

		return jsonString;

	}

	
	
	// Return true on success, return false on failure
	public static boolean checkCredentials(Credentials credentials) {

		try {

			// Establish connection
			Connection connection = connectionPool.getConnection();

			// Execute prepared statement
			PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM user_data WHERE username=? AND password=?");
			statement.setObject(1, credentials.username);
			statement.setObject(2, credentials.password);
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
	
	
	
	
	
	
	public static void main(String[] args) {
		databaseCreateTables();
	}

}
