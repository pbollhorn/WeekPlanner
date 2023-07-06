package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Controller() {
		// For debugging purposes
		System.out.println("Running Controller constructor");
		System.out.println("Default encoding for this Java installation: " + Charset.defaultCharset().displayName());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (Authenticator.checkForCredentials(request) == null) {
			RequestDispatcher view = request.getRequestDispatcher("login.html");
			view.forward(request, response);
		} else {
			RequestDispatcher view = request.getRequestDispatcher("view.html");
			view.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		
		
		System.out.println("Controller has recieved POST request with action="+action);

		if (action.equals("login")) {

			Authenticator.loginRequest(request, response);

		} else if (action.equals("logout")) {

			Authenticator.logoutRequest(request, response);

		} else if (action.equals("loaddata")) {

			Credentials credentials = Authenticator.checkForCredentials(request);

			String jsonString = Model.loadData(credentials);
			System.out.println(jsonString);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);

		} else if (action.equals("savedata")) {

			Credentials credentials = Authenticator.checkForCredentials(request);
			
			// Read JSON data from the request's input stream
			BufferedReader reader = request.getReader();
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
			String jsonString = new Gson().toJson(jsonObject);

			// Save the read in json data to database
			Model.saveData(credentials, jsonString);

		}

		return;

	}

}
