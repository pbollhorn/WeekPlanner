package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Authenticator {

	public static Credentials checkForCredentials(HttpServletRequest request) {

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

		if (username != null && password != null) {
			return new Credentials(username,password);
		} else {
			return null;
		}

	}

	public static void loginRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

		// Use Gson to deserialize the JSON into a Java credentials object
		Gson gson = new Gson();
		Credentials credentials = gson.fromJson(jsonObject, Credentials.class);

		// Print the read in credentials
		System.out.println(credentials.username);
		System.out.println(credentials.password);

		// Attempt to read plan from database. Plan will be null if failed.
		String plan = Model.loadData(credentials);

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

	}
	
	
	public static void logoutRequest(HttpServletRequest request, HttpServletResponse response) {
		
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
		
	}

}
