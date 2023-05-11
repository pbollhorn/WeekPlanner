import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	private static ArrayList<ArrayList<String>> weekPlan;

	public App()
	{

		// For debugging purposes
		System.out.println("Running constructor for App class");

		// Initialize empty weekPlan and read in data from database
		weekPlan = new ArrayList<ArrayList<String>>();
		for (int day = 0; day < 7; day++)
		{
			weekPlan.add(new ArrayList<String>());
			databaseReadDay(dayInt2Str(day));
		}

	}

	private void databaseReadDay(String day)
	{

		day = day.toLowerCase();
		ArrayList<String> dayPlan = weekPlan.get(dayStr2Int(day));
		dayPlan.clear(); // TODO: Is this line necessary?

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
				if (taskDescription != null)
				{
					dayPlan.add(taskDescription);
					System.out.println("id=" + id + " task_description=" + taskDescription);
				}

			}

			// Close connection
			connection.close();

		} catch (Exception e)
		{
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}

	}

	private void databaseUpdateDay(String day)
	{

		day = day.toLowerCase();
		ArrayList<String> dayPlan = weekPlan.get(dayStr2Int(day));

		String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlanner";
		String username = "postgres";
		String password = "crawler";

		try
		{

			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");

			// Loop over tasks and update appropriate table with the new day plan
			for (int task = 0; task < dayPlan.size(); task++)
			{
				String sql = "UPDATE " + day + " SET task_description = '" + dayPlan.get(task) + "' WHERE task_order = "
						+ task;
				System.out.println(sql);
				Statement statement = connection.createStatement();
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

	private String dayInt2Str(int day)
	{
		switch (day)
		{
		case 0:
			return "Monday";
		case 1:
			return "Tuesday";
		case 2:
			return "Wednesday";
		case 3:
			return "Thursday";
		case 4:
			return "Friday";
		case 5:
			return "Saturday";
		case 6:
			return "Sunday";
		}
		return "Error";
	}

	private int dayStr2Int(String day)
	{

		day = day.toLowerCase();

		switch (day)
		{
		case "monday":
			return 0;
		case "tuesday":
			return 1;
		case "wednesday":
			return 2;
		case "thursday":
			return 3;
		case "friday":
			return 4;
		case "saturday":
			return 5;
		case "sunday":
			return 6;
		}
		return -1;
	}

	private void planDay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String day = request.getParameter("day");
		ArrayList<String> dayPlan = weekPlan.get(dayStr2Int(day));

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.println("<html><body>");
		pw.println("<form method=\"post\" action=\"App\">");
		pw.println("<b>Plan " + day + ":</b><br>");
		pw.println("<input type=\"hidden\" name=\"action\" value=\"submitDay\" />");
		pw.println("<input type=\"hidden\" name=\"day\" value=\"" + day + "\" />");

		int i = 0;
		// Write input boxes with existing tasks from dayPlan
		for (; i < dayPlan.size(); i++)
		{
			pw.println("<input type=\"text\" name=\"task" + i + "\" value=\"" + dayPlan.get(i) + "\"><br>");
		}
		// Write empty input boxes up to total number of 10
		for (; i < 10; i++)
		{
			pw.println("<input type=\"text\" name=\"task" + i + "\"><br>");
		}

		pw.println("<input type=submit><input type=reset value=\"Undo changes\">");
		pw.println("</form>");
		pw.println("</html></body>");
		pw.close();
	}

	private void submitDay(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		int day = dayStr2Int(request.getParameter("day"));

		// Get dayPlan and clear old content
		ArrayList<String> dayPlan = weekPlan.get(day);
		dayPlan.clear();

		// Add new content to dayPlan
		for (int i = 0; i < 10; i++)
		{
			String task = request.getParameter("task" + i);

			// if task is not an empty string, then add it to dayPlan
			if (!task.isEmpty())
				dayPlan.add(task);
		}

		// HERE I SHOULD UPDATE DATABASE
		databaseUpdateDay(dayInt2Str(day));

		// Go back to front page
		response.sendRedirect("index.html");
	}

	private void showWeek(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.println("<html><body>");
		pw.println("<h1>Your week plan</h1>");
		pw.println("<a href=\"index.html\">Back to front page</a><br><br>");

		// Loop through days in weekPlan
		for (int day = 0; day < 7; day++)
		{

			pw.println("<b>" + dayInt2Str(day) + "</b><br>");
			ArrayList<String> dayPlan = weekPlan.get(day);

			// Loop through tasks in dayPlan and write to screen
			for (int i = 0; i < dayPlan.size(); i++)
			{
				pw.println("- " + dayPlan.get(i) + "<br>");
			}
			pw.println("<br>");
		}
		pw.println("</html></body>");
		pw.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String action = request.getParameter("action");

		// TODO: This avoid NullPointer exception if I accidently run App.java instead
		// of the project
		if (action == null)
			return;

		switch (action)
		{
		case "planDay":
			planDay(request, response);
			break;
		case "showWeek":
			showWeek(request, response);
			break;
		}

	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String action = request.getParameter("action");

		// TODO: This avoid NullPointer exception if I accidently run App.java instead
		// of the project
		if (action == null)
			return;

		switch (action)
		{
		case "submitDay":
			submitDay(request, response);
			break;
		}

	}

}
