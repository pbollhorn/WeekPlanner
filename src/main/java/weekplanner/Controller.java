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
	// TODO: Nyt her i version 0.5.0:
	// TODO: Hele uge planen skal være på forsiden
	// TODO: Hver task skal være på sin egen form, så jeg ved hvilken task POST request kommer fra
	// TODO: Forsæt med at indarbejde den nye SQL struktur. Husk at tilføje order for list.
	// TODO: Når der skal læses fra Databasen, så læs det hele hver gang
	// TODO: Når der skal skrives til Databasen, så skriv hver enkelt individul ting med det samme

		
	private static final long serialVersionUID = 1L;

	public static ArrayList<TaskList> taskLists;
	
	

	public Controller()
	{
		// For debugging purposes
		System.out.println("Running Controller constructor");

		// Initialize empty tasksLists and read in data from database
		taskLists = new ArrayList<TaskList>();
		Model.databaseReadEverything();
		
		
		// For debugging: Print out taskLists so we know it is read in correctly
		System.out.println("HERE IS THE NEW TASKLIST:");
		for(int i=0; i<taskLists.size(); i++)
		{
			System.out.println(taskLists.get(i).name);
			
			ArrayList<String> list = taskLists.get(i).tasks;
			
			for(int j=0;j<list.size();j++)
				System.out.println("- "+list.get(j));
			
		}
		System.out.println("NEW TASKLIST IS NOW OVER");
	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		RequestDispatcher dispatcher = request.getRequestDispatcher("/view.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String listNumber = request.getParameter("listNumber");
		String taskNumber = request.getParameter("taskNumber");
		String taskDescription = request.getParameter("taskDescription");
		String save = request.getParameter("save");
		String delete = request.getParameter("delete");
		String moveUp = request.getParameter("moveUp");
		String moveDown = request.getParameter("moveDown");
		String add = request.getParameter("add");
		
		
		
		
		// For debugging
		System.out.println(request.getParameterMap().keySet());
		System.out.println("listNumber= "+listNumber);
		System.out.println("taskNumber= "+taskNumber);
		System.out.println("taskDescription= "+taskDescription);
		System.out.println("save= "+save);
		System.out.println("delete= "+delete);
		System.out.println("moveUp= "+moveUp);
		System.out.println("moveDown= "+moveDown);
		System.out.println("add= "+add);
		
		// Catch the buttons pressed
		if(add!=null)
		{
			Model.addTask(Integer.parseInt(listNumber));
		}
		
		if(delete!=null)
		{
			Model.deleteTask(Integer.parseInt(listNumber),Integer.parseInt(taskNumber));
		}
		
		
		// Read everything fresh from the database and show the view again
		Model.databaseReadEverything();
		RequestDispatcher dispatcher = request.getRequestDispatcher("/view.jsp");
		dispatcher.forward(request, response);

		return;
		
		
//		// Get submit buttons and hidden input
//		String day = request.getParameter("submitDay");
//		String save = request.getParameter("save");
//		String add = request.getParameter("add");
//		
//		ArrayList<String> dayPlan = weekPlan.get(dayStr2Int(day));
//		dayPlan.clear();
//		
//		// Loop over tasks and break when a null task is met
//		for(int i=0;;i++)
//		{
//			String task_description = request.getParameter("task"+i);
//			if( task_description == null)
//				break;
//			
//			// Trim the task description of leading and trailing whitespace
//			task_description = task_description.trim();
//				
//			// If task description is not an empty string, then put it in dayPlan
//			if( ! task_description.isEmpty() )
//			{
//				dayPlan.add(task_description);
//			}
//				
//		}
//		
//		// Now update database
//		Model.databaseUpdateDay(day, dayPlan);		
//		
//		
//		// Save button is pressed
//		if(save!=null)
//		{		
//			// Forward to front page
//			doGet(request, response);
//			
//			// For debugging
//			System.out.println("Reached end of if(save!=null) statement");
//		}
//		
//		
//		// Input button is pressed
//		if(add!=null)
//		{
//			// Add an empty task to dayPlan
//			dayPlan.add("");
//			
//			// Show plan_day.jsp again
//			RequestDispatcher dispatcher = request.getRequestDispatcher("/plan_day.jsp?day="+day);
//			dispatcher.forward(request, response);
//			
//			// For debugging
//			System.out.println("Reached end of if(add!=null) statement");
//			
//		}
		
		
		

	}

}
