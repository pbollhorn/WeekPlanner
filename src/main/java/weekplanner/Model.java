package weekplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Model {

	private static String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlannerDB3";
	//private static String jdbcURL = "jdbc:postgresql://16.16.155.85:5432/WeekPlannerDB3";
	private static String username = "postgres";
	private static String password = "crawler";

	public static void databaseCreateTables() {

		try {
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
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

	public static void databasePostData(String jsonString) {

		try {
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");

			// Update user_data via prepared statement
			PreparedStatement statement = connection.prepareStatement("UPDATE user_data SET plan=? WHERE username='egon'");
			statement.setObject(1, jsonString, java.sql.Types.OTHER);
			statement.execute();

			// Close connection
			statement.close();
			connection.close();

		} catch (Exception e) {
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}

	}

	public static String databaseGetData() {

		String jsonString = null;

		try {

			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");

			// Get plan from user_data
			Statement statement = connection.createStatement();
			String sql = "SELECT plan FROM user_data WHERE username='egon'";
			ResultSet result = statement.executeQuery(sql);
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

	public static void main(String[] args) {
		databaseCreateTables();
	}

}
