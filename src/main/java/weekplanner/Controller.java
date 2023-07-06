package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

		String username = null;
		String password = null;

		Cookie[] theCookies = request.getCookies();

		if (theCookies != null) {
			for (Cookie tempCookie : theCookies) {
				if ("WeekPlannerUsername".equals(tempCookie.getName())) {
					username = tempCookie.getValue();
				} else if ("WeekPlannerPassword".equals(tempCookie.getName())) {
					password = tempCookie.getValue();
				}
			}
		}

		System.out.println(username);
		System.out.println(password);
		
		
		String jsonString = Model.databaseGetData(username, password);
		System.out.println(jsonString);

		if (jsonString == null) {
			RequestDispatcher view = request.getRequestDispatcher("login.html");
			view.forward(request, response);
		} else {
			
			RequestDispatcher view = request.getRequestDispatcher("view.html");
			view.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Controller has been called");

		String action = request.getParameter("action");

		if (action.equals("login")) {
			System.out.println("action is login");

			// Read JSON data from the request's input stream
			BufferedReader reader = request.getReader();
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

			// Use Gson to deserialize the JSON into a Java credentials object
			Gson gson = new Gson();
			Credentials credentials = gson.fromJson(jsonObject, Credentials.class);

			// Print the read in data
			System.out.println(credentials.username);
			System.out.println(credentials.password);

			String plan = Model.databaseGetData(credentials.username, credentials.password);

			// If the credentials are valid, set the response status to indicated success,
			// else set response status to indicate failure
			if (plan != null) {
				System.out.println("Valid Credentials - Access granted");
				response.setStatus(HttpServletResponse.SC_OK);

				// Create cookies with username and password
				Cookie theCookie = new Cookie("WeekPlannerUsername", credentials.username);
				theCookie.setMaxAge(60 * 60 * 24 * 365);
				response.addCookie(theCookie);
				theCookie = new Cookie("WeekPlannerPassword", credentials.password);
				theCookie.setMaxAge(60 * 60 * 24 * 365);
				response.addCookie(theCookie);

			} else {
				System.out.println("Invalid Credentials - Access denied");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} else if (action.equals("logout")) {

			System.out.println("action is logout");
			response.setStatus(HttpServletResponse.SC_OK);
			
			// Delete cookies
			Cookie[] theCookies = request.getCookies();
			if (theCookies != null) {
				for (Cookie tempCookie : theCookies) {
					if ("WeekPlannerUsername".equals(tempCookie.getName())) {
						tempCookie.setMaxAge(0);
						response.addCookie(tempCookie);
					} else if ("WeekPlannerPassword".equals(tempCookie.getName())) {
						tempCookie.setMaxAge(0);
						response.addCookie(tempCookie);
					}
				}
			}
			
			
			

		} else if (action.equals("loaddata")) {

			System.out.println("action is loaddata");

			String username = null;
			String password = null;
			
			Cookie[] theCookies = request.getCookies();

			if (theCookies != null) {
				for (Cookie tempCookie : theCookies) {
					if ("WeekPlannerUsername".equals(tempCookie.getName())) {
						username = tempCookie.getValue();
					} else if ("WeekPlannerPassword".equals(tempCookie.getName())) {
						password = tempCookie.getValue();
					}
				}
			}
			
			String jsonString = Model.databaseGetData(username, password);
			System.out.println(jsonString);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);

		} else if (action.equals("savedata")) {

			System.out.println("action is savedata");

			// Read JSON data from the request's input stream
			BufferedReader reader = request.getReader();
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
			String jsonString = new Gson().toJson(jsonObject);

			// Post the read in json data to database
			Model.databasePostData(jsonString);

		}

		return;

	}

}
