package weekplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Model
{
	private static String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlannerDB2";
	//private static String jdbcURL = "jdbc:postgresql://16.16.155.85:5432/WeekPlannerDB2";
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
			sql = "CREATE TABLE list(id SERIAL PRIMARY KEY, name TEXT, number INT)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Monday', 0)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Tuesday', 1)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Wednesday', 2)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Thursday', 3)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Friday', 4)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Saturday', 5)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Sunday', 6)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Within next week', 7)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Within a month', 8)";
			System.out.println(sql);
			statement.execute(sql);
			sql = "INSERT INTO list (name, number) VALUES ('Within a year', 9)";
			System.out.println(sql);
			statement.execute(sql);
			
			
			// Create task table
			sql = "CREATE TABLE task(id SERIAL PRIMARY KEY, description TEXT, number INT, list_id INT, CONSTRAINT fk_list FOREIGN KEY(list_id) REFERENCES list(id))";
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

			// Start by reading in all list_id and names into taskLists
			String sql = "SELECT * FROM list ORDER BY number ASC";
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
				
				sql = "SELECT description FROM task WHERE list_id=" + id +" ORDER BY number ASC";
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
	
	
	
	public static void moveTaskDown(int listNumber, int taskNumber)
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
			sql = "SELECT id FROM list WHERE number="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}
			
			// Get current count of tasks
			int count = -1;
			sql = "SELECT COUNT(number) FROM task WHERE list_id="+list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				count = result.getInt(1);
			}
			
			
			// Return immediately if taskNumber is >= count-1, because then task cannot be moved further down
			if(taskNumber>=count-1)
				return;
			
			
			// Get task_id of task to be moved down
			int task_id_A = -1;
			sql = "SELECT id FROM task WHERE number=" + taskNumber + " AND list_id=" + list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				task_id_A = result.getInt(1);
			}
			
			// Get task_id of task to be moved up
			int task_id_B = -1;
			sql = "SELECT id FROM task WHERE number=" + (taskNumber+1) + " AND list_id=" + list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				task_id_B = result.getInt(1);
			}
			
			
			// Update task_id_A
			sql = "UPDATE task SET number=number+1 WHERE id=" + task_id_A + " AND list_id="+list_id;
			System.out.println(sql);
			statement.execute(sql);			

			// Update task_id_B
			sql = "UPDATE task SET number=number-1 WHERE id=" + task_id_B + " AND list_id="+list_id;
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
	
	
	
	public static void moveTaskUp(int listNumber, int taskNumber)
	{
	
		// Return immediately if taskNumber is 0, because then task cannot be moved further up
		if(taskNumber==0)
			return;
		
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
			sql = "SELECT id FROM list WHERE number="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}			
			
			
			// Get task_id of task to be moved up
			int task_id_A = -1;
			sql = "SELECT id FROM task WHERE number=" + taskNumber + " AND list_id=" + list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				task_id_A = result.getInt(1);
			}
			
			// Get task_id of task to be moved down
			int task_id_B = -1;
			sql = "SELECT id FROM task WHERE number=" + (taskNumber-1) + " AND list_id=" + list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				task_id_B = result.getInt(1);
			}
			
			
			// Update task_id_A
			sql = "UPDATE task SET number=number-1 WHERE id=" + task_id_A + " AND list_id="+list_id;
			System.out.println(sql);
			statement.execute(sql);			

			// Update task_id_B
			sql = "UPDATE task SET number=number+1 WHERE id=" + task_id_B + " AND list_id="+list_id;
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
	
	
	
	public static void saveTask(int listNumber, int taskNumber, String taskDescription)
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
			sql = "SELECT id FROM list WHERE number="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}			
			
			// Update task
			sql = "UPDATE task SET description='" + taskDescription + "' WHERE number="+taskNumber+" AND list_id="+list_id;
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
			sql = "SELECT id FROM list WHERE number="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}
			System.out.println(list_id);
					
			// Delete task
			sql = "DELETE FROM task WHERE number=" + taskNumber + " AND list_id="+list_id;
			System.out.println(sql);
			statement.execute(sql);
			
			// Change order of the other tasks
			sql = "UPDATE task SET number=number-1 WHERE number>" + taskNumber + " AND list_id="+list_id;
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
			sql = "SELECT id FROM list WHERE number="+listNumber;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				list_id = result.getInt(1);
			}
			
			// Get current count of tasks
			int count = -1;
			sql = "SELECT COUNT(number) FROM task WHERE list_id="+list_id;
			System.out.println(sql);
			result = statement.executeQuery(sql);
			if(result.next())
			{
				count = result.getInt(1);
			}
					
			// Add new empty task at end of task list, number for the new task is "count"
			sql = "INSERT INTO task (description, number, list_id) VALUES ('[EMPTY]', " + count + ", " + list_id +")";
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
	
	
		
	public static void main(String[] args)
	{
		databaseCreateTables();		
	}

}
