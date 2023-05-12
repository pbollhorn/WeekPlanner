package weekplanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Model
{
	public static ArrayList<String> databaseReadDay(String day)
	{
		day = day.toLowerCase();
		ArrayList<String> dayPlan = new ArrayList<String>();

		String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlanner";
		String username = "postgres";
		String password = "crawler";

		try
		{

			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");

			// Read day plan from appropriate table
			String sql = "SELECT * FROM " + day + " ORDER BY task_order ASC";
			System.out.println(sql);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next())
			{
				int id = result.getInt("id");
				String taskDescription = result.getString("task_description");
				dayPlan.add(taskDescription);
				System.out.println("id=" + id + " task_description=" + taskDescription);
			}

			
			// Close connection
			connection.close();

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}
		
		
		return dayPlan;

	}
	
	
	
	
	public static void databaseUpdateDay(String day,ArrayList<String> dayPlan)
	{
		day = day.toLowerCase();

		String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlanner";
		String username = "postgres";
		String password = "crawler";

		try
		{
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");

			
			// Delete all rows in day table without deleting the table 
			String sql = "DELETE FROM " + day;
			System.out.println(sql);
			Statement statement = connection.createStatement();
			statement.execute(sql);
			
			
			// Loop over tasks and update appropriate table with the new day plan
			for (int task = 0; task < dayPlan.size(); task++)
			{
				sql = "INSERT INTO " + day + " (task_description,task_order) VALUES ('" + dayPlan.get(task) + "',"+task+")";
				System.out.println(sql);
				statement.execute(sql);
			}

			// Close connection
			connection.close();

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}

	}

	
	
	
}
