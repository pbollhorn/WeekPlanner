package weekplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Model
{
	private static String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlannerDB2";
	private static String username = "postgres";
	private static String password = "crawler";
	
	public static void databaseCreateTables()
	{

		try
		{
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");
			Statement statement = connection.createStatement();
			String sql;
			
			// Create list table including the Monday - Sunday lists
			sql = "CREATE TABLE list(id SERIAL PRIMARY KEY, name TEXT, \"order\" INT)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Monday', 0)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Tuesday', 1)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Wednesday', 2)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Thursday', 3)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Friday', 4)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Saturday', 5)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Sunday', 6)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Within next week', 7)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Within a month', 8)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, \"order\") VALUES ('Within a year', 9)";
			System.out.println(sql);
			statement.execute(sql);
			
			
			// Create task table
			sql = "CREATE TABLE task(id SERIAL PRIMARY KEY, description TEXT, \"order\" INT, list_id INT, CONSTRAINT fk_list FOREIGN KEY(list_id) REFERENCES list(id))";
			System.out.println(sql);
			statement.execute(sql);

			// Close connection
			statement.close();
			connection.close();
			
			

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void databaseReadEverything()
	{

		Controller.taskLists.clear();

		try
		{

			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");
			Statement statement = connection.createStatement();
			ResultSet result;

			// Start by reading in all list id and names into taskLists
			String sql = "SELECT * FROM list ORDER BY \"order\" ASC";
			System.out.println(sql);
			result = statement.executeQuery(sql);
			while (result.next())
			{
				int id = result.getInt("id");
				String name = result.getString("name");
				
				Controller.taskLists.add(new TaskList(id, name));
			}			
			
			// Then read content of the lists into taskLists
			for(int i=0; i<Controller.taskLists.size(); i++)
			{
				int id = Controller.taskLists.get(i).id;
				
				sql = "SELECT description FROM task WHERE list_id=" + id +" ORDER BY \"order\" ASC";
				System.out.println(sql);
				result = statement.executeQuery(sql);
				while (result.next())
				{
					String description = result.getString("description");		
					Controller.taskLists.get(i).tasks.add(description);
				}				
				
				
			
			}
			
			// Close connection
			statement.close();
			result.close();
			connection.close();
			

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}
		

	}
	
	
	public static void deleteTask(int listNumber, int taskNumber)
	{
		
		try
		{
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");
			Statement statement = connection.createStatement();
			ResultSet result;
			String sql;
			
			// Get list_id
			int list_id= -1;
			sql = "SELECT id FROM list WHERE \"order\"="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}
			System.out.println(list_id);
					
			// Delete task
			sql = "DELETE FROM task WHERE \"order\"=" + taskNumber + " AND list_id="+list_id;
			System.out.println(sql);
			statement.execute(sql);
			
			// Change order of the other tasks
			sql = "UPDATE task SET \"order\"=\"order\"-1 WHERE \"order\">" + taskNumber + " AND list_id="+list_id;
			System.out.println(sql);
			statement.execute(sql);
			
			// Close connection
			result.close();
			statement.close();
			connection.close();
			
			

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	public static void addTask(int listNumber)
	{
		
		try
		{
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");
			Statement statement = connection.createStatement();
			ResultSet result;
			String sql;
			
			// Get list_id
			int list_id= -1;
			sql = "SELECT id FROM list WHERE \"order\"="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}
			System.out.println(list_id);
			
			// Get max task number
			int max = -1;
			sql = "SELECT MAX(\"order\") FROM task WHERE list_id="+list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				max = result.getInt(1);
			}
			System.out.println(max);
					
			// Add new empty task at end of task list
			sql = "INSERT INTO task (description, \"order\", list_id) VALUES ('', " + max + ", " + list_id +")";
			System.out.println(sql);
			statement.execute(sql);
			
			// Close connection
			result.close();
			statement.close();
			connection.close();
			
			

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}

		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
//	public static ArrayList<String> databaseReadDay(String day)
//	{
//		day = day.toLowerCase();
//		ArrayList<String> dayPlan = new ArrayList<String>();
//
//		String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlanner";
//		String username = "postgres";
//		String password = "crawler";
//
//		try
//		{
//
//			// Establish connection
//			Class.forName("org.postgresql.Driver");
//			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
//			System.out.println("Connected to PostgreSQL");
//
//			// Read day plan from appropriate table
//			String sql = "SELECT * FROM " + day + " ORDER BY task_order ASC";
//			System.out.println(sql);
//			Statement statement = connection.createStatement();
//			ResultSet result = statement.executeQuery(sql);
//			while (result.next())
//			{
//				int id = result.getInt("id");
//				String taskDescription = result.getString("task_description");
//				dayPlan.add(taskDescription);
//				System.out.println("id=" + id + " task_description=" + taskDescription);
//			}
//
//			
//			// Close connection
//			connection.close();
//
//		} catch (Exception e)
//		{
//			System.out.println("Error in connecting to PostgreSQL server");
//			e.printStackTrace();
//		}
//		
//		
//		return dayPlan;
//
//	}
	
	
	
	
//	public static void databaseUpdateDay(String day,ArrayList<String> dayPlan)
//	{
//		day = day.toLowerCase();
//
//		String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlanner";
//		String username = "postgres";
//		String password = "crawler";
//
//		try
//		{
//			// Establish connection
//			Class.forName("org.postgresql.Driver");
//			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
//			System.out.println("Connected to PostgreSQL");
//
//			
//			// Delete all rows in day table without deleting the table 
//			String sql = "DELETE FROM " + day;
//			System.out.println(sql);
//			Statement statement = connection.createStatement();
//			statement.execute(sql);
//			
//			
//			// Loop over tasks and update appropriate table with the new day plan
//			for (int task = 0; task < dayPlan.size(); task++)
//			{
//				sql = "INSERT INTO " + day + " (task_description,task_order) VALUES ('" + dayPlan.get(task) + "',"+task+")";
//				System.out.println(sql);
//				statement.execute(sql);
//			}
//
//			// Close connection
//			connection.close();
//
//		} catch (Exception e)
//		{
//			System.out.println("Error in connecting to PostgreSQL server");
//			e.printStackTrace();
//		}
//
//	}

	
	public static void main(String[] args)
	{
		
		databaseCreateTables();
		
		
	}
	
	
	
}
