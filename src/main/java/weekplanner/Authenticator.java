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

		String credentials = null;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {

			for (Cookie c : cookies) {
				if ("WeekPlannerCredentials".equals(c.getName())) {
					credentials = c.getValue();
					break;
				}
			}

			if (credentials != null) {

				int index = credentials.indexOf('=');
				String username = credentials.substring(0, index);
				String password = credentials.substring(index + 1);

				if (username != null && password != null) {
					return new Credentials(username, password);
				}

			}

		}

		return null;

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

			// Create cookie with credentials (username=password)
			Cookie cookie = new Cookie("WeekPlannerCredentials", credentials.username + "=" + credentials.password);
			cookie.setMaxAge(60 * 60 * 24 * 365);
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);

		} else {
			System.out.println("Invalid Credentials - Access denied");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

	}

	public static void logoutRequest(HttpServletRequest request, HttpServletResponse response) {

		// Find the credentials cookie and delete it
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if ("WeekPlannerCredentials".equals(c.getName())) {
					c.setMaxAge(0);
					c.setPath(request.getContextPath());
					response.addCookie(c);
					break;
				}
			}
		}

		response.setStatus(HttpServletResponse.SC_OK);

	}

}