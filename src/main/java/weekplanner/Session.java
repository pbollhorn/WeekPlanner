package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Session {

	// Returns Credentials object if Credentials cookie is present, else returns null
	public static Credentials getCredentialsFromCookie(HttpServletRequest request) {

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

	// GET session request is received from index.html, because index.html wants to check if a user is logged in or not
	// Return status code 200 if user is logged in, otherwise status code 401
	public static void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (Session.getCredentialsFromCookie(request) == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}
	

	// This is equivalent to login
	public static void post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

		// Use Gson to deserialize the JSON into a Java credentials object
		Gson gson = new Gson();
		Credentials credentials = gson.fromJson(jsonObject, Credentials.class);

		// Check if credentials are in database
		boolean status = Model.checkCredentials(credentials);

		// If status == false, respond with status code SC_UNAUTHORIZED
		if (status == false) {
			System.out.println("Invalid Credentials - Access denied");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

		// Since status == true, the credentials are valid,
		// Create and send credentials cookie with value "username=password"
		// and set response status to indicated success
		Cookie cookie = new Cookie("WeekPlannerCredentials", credentials.username + "=" + credentials.password);
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	// This is equivalent to logout
	public static void delete(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = new Cookie("WeekPlannerCredentials", "");
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(0);

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);

	}

}