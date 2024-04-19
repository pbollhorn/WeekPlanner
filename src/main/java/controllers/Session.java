package controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Database;

public class Session {

	private static final int SESSION_TIMEOUT_SECONDS = 86400;

	/**
	 * Gets userId from session, or 0 if there is no session. Also renews expiration time JSESSIONID of cookie.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 * @return userId for session, or 0 if no session
	 */
	public static int getUserId(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return 0;
		}

		Object object = session.getAttribute("userId");
		if (object == null) {
			return 0;
		}

		// Get the JSESSIONID cookie.
		// And renew the expiration of the cookie, so it is the same as the session timeout.
		Cookie[] cookies = request.getCookies();
		Cookie jsessionidCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equals(cookie.getName())) {
					jsessionidCookie = cookie;
					break;
				}
			}
		}
		if (jsessionidCookie != null) {
			jsessionidCookie.setMaxAge(SESSION_TIMEOUT_SECONDS);
			jsessionidCookie.setPath(request.getContextPath());
			response.addCookie(jsessionidCookie);
		}

		System.out.println(session.getMaxInactiveInterval());

		int userId = (int) object;
		return userId;
	}

	/**
	 * API endpoint POST session. Used for login. Returns JSESSIONID cookie that expires when browser closes.
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

		// Get userId from database
		int userId = Database.getUserId(credentials);
		if (userId == 0) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Invalidate old session, if there is an old session
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("userId");
			session.invalidate();
		}

		// Create new session and put userId inside it
		session = request.getSession(true);
		session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
		session.setAttribute("userId", userId);

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
			session.removeAttribute("userId");
			session.invalidate();
		}

		response.setStatus(HttpServletResponse.SC_OK);

	}

}