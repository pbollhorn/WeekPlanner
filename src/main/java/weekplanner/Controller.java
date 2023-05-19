package weekplanner;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.Charset;

public class Controller extends HttpServlet
{
	// TODO: Nyt her i version 0.5.0:
	// TODO: Hele uge planen skal være på forsiden
	// TODO: Hver task skal være på sin egen form, så jeg ved hvilken task POST request kommer fra
	// TODO: Forsæt med at indarbejde den nye SQL struktur. Husk at tilføje number til list.
	// TODO: Når der skal læses fra Databasen, så læs det hele hver gang
	// TODO: Når der skal skrives til Databasen, så skriv hver enkelt individul ting med det samme

		
	private static final long serialVersionUID = 1L;

	public static ArrayList<TaskList> taskLists;
	
	

	public Controller()
	{
		// For debugging purposes
		System.out.println("Running Controller constructor");
		Charset charset = Charset.defaultCharset();
        System.out.println("Default encoding for this Java installation: " + charset.displayName());
		

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
		// Get inputs from form: text, hidden input, and submit buttons
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
		
		// Identify the submit button pressed
		if(add!=null)
		{
			Model.addTask(Integer.parseInt(listNumber));
		}
		else if(delete!=null)
		{
			Model.deleteTask(Integer.parseInt(listNumber),Integer.parseInt(taskNumber));
		}
		else if(save!=null)
		{
			Model.saveTask(Integer.parseInt(listNumber), Integer.parseInt(taskNumber), taskDescription);
		}
		else if(moveUp!=null)
		{
			Model.moveTaskUp(Integer.parseInt(listNumber), Integer.parseInt(taskNumber));
		}
		else if(moveDown!=null)
		{
			Model.moveTaskDown(Integer.parseInt(listNumber), Integer.parseInt(taskNumber));
		}		
		
		// Read everything fresh from the database and show the view again
		Model.databaseReadEverything();
		RequestDispatcher dispatcher = request.getRequestDispatcher("/view.jsp");
		dispatcher.forward(request, response);

		return;
		
	}

}
