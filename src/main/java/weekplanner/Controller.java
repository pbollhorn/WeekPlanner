package weekplanner;
import java.io.IOException;  
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Controller extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<ArrayList<String>> weekPlan;
	
	public static String dayInt2Str(int day)
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

	public static int dayStr2Int(String day)
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
	

	public Controller()
	{
		// For debugging purposes
		System.out.println("Running Controller constructor");
		
		// Initialize empty weekPlan and read in data from database
		weekPlan = new ArrayList<ArrayList<String>>();
		for (int day = 0; day < 7; day++)
		{
			weekPlan.add( Model.databaseReadDay(dayInt2Str(day)) );
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Get submit buttons and hidden input
		String day = request.getParameter("submitDay");
		String save = request.getParameter("save");
		String add = request.getParameter("add");
		
		ArrayList<String> dayPlan = weekPlan.get(dayStr2Int(day));
		dayPlan.clear();
		
		// Loop over tasks and break when a null task is met
		for(int i=0;;i++)
		{
			String task_description = request.getParameter("task"+i);
			if( task_description == null)
				break;
			
			// Trim the task description of leading and trailing whitespace
			task_description = task_description.trim();
				
			// If task description is not an empty string, then put it in dayPlan
			if( ! task_description.isEmpty() )
			{
				dayPlan.add(task_description);
			}
				
		}
		
		// Now update database
		Model.databaseUpdateDay(day, dayPlan);		
		
		
		// Save button is pressed
		if(save!=null)
		{		
			// Forward to front page
			doGet(request, response);
			
			// For debugging
			System.out.println("Reached end of if(save!=null) statement");
		}
		
		
		// Input button is pressed
		if(add!=null)
		{
			// Add an empty task to dayPlan
			dayPlan.add("");
			
			// Show plan_day.jsp again
			RequestDispatcher dispatcher = request.getRequestDispatcher("/plan_day.jsp?day="+day);
			dispatcher.forward(request, response);
			
			// For debugging
			System.out.println("Reached end of if(add!=null) statement");
			
		}
		
		
		
		
	

		
	}

}
