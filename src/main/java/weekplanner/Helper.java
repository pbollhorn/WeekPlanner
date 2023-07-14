package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Helper {

	// Top level GET request, i.e. when the user types pbollhorn.dk/weekplanner in their browser.
	public static void topLevelGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (Authenticator.checkForCredentials(request) == null) {
			RequestDispatcher view = request.getRequestDispatcher("/login.html");
			view.forward(request, response);
		} else {
			RequestDispatcher view = request.getRequestDispatcher("/view.html");
			view.forward(request, response);
		}
	}

	public static void hello(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (Authenticator.checkForCredentials(request) == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			RequestDispatcher view = request.getRequestDispatcher("/login.html");
			view.forward(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
			RequestDispatcher view = request.getRequestDispatcher("/view.html");
			view.forward(request, response);
		}
	}
	
	public static void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Authenticator.loginRequest(request, response);
		RequestDispatcher view = request.getRequestDispatcher("/view.html");
		view.forward(request, response);
	}

	public static void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Authenticator.logoutRequest(request, response);

		RequestDispatcher view = request.getRequestDispatcher("/login.html");
		view.forward(request, response);
	}

	public static void loadData(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Credentials credentials = Authenticator.checkForCredentials(request);

		String jsonString = Model.loadData(credentials);
		System.out.println(jsonString);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);

	}

	public static void saveData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Credentials credentials = Authenticator.checkForCredentials(request);

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
		String jsonString = new Gson().toJson(jsonObject);

		// Save the read in json data to database
		Model.saveData(credentials, jsonString);
	}

}
