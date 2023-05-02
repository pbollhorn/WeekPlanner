import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static ArrayList<ArrayList<String>> weekPlan;

	public App() {

		// For debugging purposes
		System.out.println("Running constructor for App class");

		// Initialize weekPlan
		weekPlan = new ArrayList<ArrayList<String>>();
		for (int day = 0; day < 7; day++)
			weekPlan.add(new ArrayList<String>());
	}

	
	private void databaseTest() {
		
		String jdbcURL = "jdbc:postgresql://localhost:5432/WeekPlanner";
		String username = "postgres";
		String password = "crawler";
		
		try {
			
			// Establish connection
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connected to PostgreSQL");
			
			// Do some stuff
			String sql = "SELECT * FROM monday";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next() ) {
				int id = result.getInt("id");
				String taskDescription = result.getString("task_description");
				
				if( taskDescription != null)
					System.out.println("id="+id+" "+taskDescription+" "+taskDescription.length());
				else
					System.out.println("id="+id+" [Empty String]");
			}
					
			// Close connection
			connection.close();
		
		} catch(Exception e) {
			System.out.println("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}	
		
		
	}
	
	
	
	private String dayInt2Str(int day) {
		switch (day) {
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

	private int dayStr2Int(String day) {
		switch (day) {
		case "Monday":
			return 0;
		case "Tuesday":
			return 1;
		case "Wednesday":
			return 2;
		case "Thursday":
			return 3;
		case "Friday":
			return 4;
		case "Saturday":
			return 5;
		case "Sunday":
			return 6;
		}
		return -1;
	}

	private void planDay(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String day = request.getParameter("day");
		ArrayList<String> dayPlan = weekPlan.get(dayStr2Int(day));

		PrintWriter pw = response.getWriter();
		pw.println("<body>"); // this somehow gets the HTML code below to work
		pw.println("<form method=\"get\" action=\"App\">");
		pw.println("<b>Plan " + day + ":</b><br>");
		pw.println("<input type=\"hidden\" name=\"action\" value=\"submitDay\" />");
		pw.println("<input type=\"hidden\" name=\"day\" value=\"" + day + "\" />");

		int i = 0;
		// Write input boxes with existing tasks from dayPlan 
		for (; i < dayPlan.size(); i++) {
			pw.println("<input type=\"text\" name=\"task" + i + "\" value=\"" + dayPlan.get(i) + "\"><br>");
		}
		// Write empty input boxes up to total number of 10
		for (; i < 10; i++) {
			pw.println("<input type=\"text\" name=\"task" + i + "\"><br>");
		}

		pw.println("<input type=submit><input type=reset value=\"Undo changes\">");
		pw.println("</form>");
		pw.close();
	}

	private void submitDay(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int day = dayStr2Int(request.getParameter("day"));

		// Get dayPlan and clear old content
		ArrayList<String> dayPlan = weekPlan.get(day);
		dayPlan.clear();

		// Add new content to dayPlan
		for (int i = 0; i < 10; i++) {
			String task = request.getParameter("task" + i);

			// if task is not an empty string, then add it to dayPlan
			if (!task.isEmpty())
				dayPlan.add(task);
		}

		// Go back to front page
		response.sendRedirect("index.html");
	}

	private void showWeek(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		pw.println("<body>"); // this somehow gets the HTML code below to work
		pw.println("<h1>Your week plan</h1>");
		pw.println("<a href=\"index.html\">Back to front page</a><br><br>");

		// Loop through days in weekPlan
		for (int day = 0; day < 7; day++) {

			pw.println("<b>" + dayInt2Str(day) + "</b><br>");
			ArrayList<String> dayPlan = weekPlan.get(day);

			// Loop through tasks in dayPlan and write to screen
			for (int i = 0; i < dayPlan.size(); i++) {
				pw.println("- " + dayPlan.get(i) + "<br>");
			}
			pw.println("<br>");
		}
		pw.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		switch (action) {
		case "planDay":
			planDay(request, response);
			break;
		case "submitDay":
			submitDay(request, response);
			break;
		case "showWeek":
			showWeek(request, response);
			break;
		}
		
		
		databaseTest();

	}

}
