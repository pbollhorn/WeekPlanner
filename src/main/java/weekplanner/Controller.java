package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static ArrayList<TaskList> taskLists;

	public Controller() {

		// For debugging purposes
		System.out.println("Running Controller constructor");
		Charset charset = Charset.defaultCharset();
		System.out.println("Default encoding for this Java installation: " + charset.displayName());

		// Initialize empty tasksLists and read in data from database
		taskLists = new ArrayList<TaskList>();
		//  Model.databaseReadEverything();

	}

	// This method runs when URL "/controller" is typed in the browser
	// and is the first thing that runs when the user opens the app in the browser
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get username from session object
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");

		// If username is not found in session object, it means user is not logged in,
		// so forward to login page
		// otherwise forward to view page
		if (username == null) {
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/view.jsp").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Controlleren er blevet kaldt");

		String action = request.getParameter("action");

		// In case of action is null, then lets read some json
		if (action == null) {

			// Read JSON data from the request's input stream
			BufferedReader reader = request.getReader();
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

			// Use Gson to deserialize the JSON into a Java object
			Gson gson = new Gson();
			Plan plan = gson.fromJson(jsonObject, Plan.class);

			// Test the read in data
			System.out.println(plan.username);

			return;
		}

		// In case action equals "login", it means the user has sent a login attempt, so
		// let's process that
		if (action.equals("login")) {

			// Get username and password from request
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			// Check if username and password are valid credentials
			if ((username.equals("peter") && password.equals("9805"))
					|| (username.equals("jens") && password.equals("1234"))) {
				// username and password are valid,
				// so store username in session object to signify that the user is logged on
				HttpSession session = request.getSession();
				session.setAttribute("username", username);

				// Clear tasklist and read everything in from database
				Model.databaseReadEverything();

				// Forward to view.jsp
				request.getRequestDispatcher("/view.jsp").forward(request, response);

			} else {
				// username and password are not valid,
				// so forward back to login.jsp with error message in request object
				request.setAttribute("errorMessage", "Wrong username or password");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}

		}

	}

}
