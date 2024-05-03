package controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Database;

public class Session {

	// private static final int SESSION_TIMEOUT_SECONDS = 86400;

	/**
	 * Gets credentials from session, or null if there is no session.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 * @return userId for session, or 0 if no session
	 */
	public static Credentials getCredentials(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		Object object = session.getAttribute("credentials");
		if (object == null) {
			return null;
		}

		System.out.println(session.getMaxInactiveInterval());

		return (Credentials) object;
	}

	/**
	 * API endpoint POST session. Used for login.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 */
	public static void post(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

		// Use Gson to deserialize the JSON into a Java credentials object
		Credentials credentials = new Gson().fromJson(jsonObject, Credentials.class);

		// Attempt to login (successful login will put hashedPassword in credentials object)
		if (Database.login(credentials) == false) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Invalidate old session, if there is an old session
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		// Create new session and put credentials inside it
		session = request.getSession(true);
		session.setAttribute("credentials", credentials);

		response.setStatus(HttpServletResponse.SC_OK);

	}

	/**
	 * API endpoint DELETE session. Used for logout.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 */
	public static void delete(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.setStatus(HttpServletResponse.SC_OK);
		
		// TODO: Perhaps send different responses wether there was a session to cancel or not

	}

}