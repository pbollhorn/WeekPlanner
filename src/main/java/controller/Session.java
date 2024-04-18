package controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Database;

public class Session {

	public static int checkAndRenewSessionNEW(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		
		if(session == null) {
			System.out.println("Session is null");
			return 0;
		}

		// Get the JSESSIONID cookie
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
			jsessionidCookie.setMaxAge(-1); // Set the expiration time in seconds
			response.addCookie(jsessionidCookie); // Add the modified cookie to the response
		}
		System.out.println(request.getContextPath());
		
		Object o = session.getAttribute("userId");
		if(o==null) {
			System.out.println("object is null");
			return 0;
		}

		int userId = (int) o;

		return userId;
	}

	// My NEW login functionality
	public static void postNEW(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

		// Use Gson to deserialize the JSON into a Java credentials object
		Gson gson = new Gson();
		Credentials credentials = gson.fromJson(jsonObject, Credentials.class);

		// Check if credentials are in database
		int userId = Database.checkCredentialsNEW(credentials);
		System.out.println("userId: " + userId);

		// If no such user, respond with status code SC_UNAUTHORIZED
		if (userId == 0) {
			System.out.println("Invalid Credentials - Access denied");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// LETS WORK WITH THE HTTPSESSION OBJECT
		// We put userId inside HttpSession object
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(86400);
		session.setAttribute("userId", userId);

		// Get the JSESSIONID cookie
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
			jsessionidCookie.setMaxAge(-1); // Set the expiration time in seconds
			jsessionidCookie.setPath(request.getContextPath());
			response.addCookie(jsessionidCookie); // Add the modified cookie to the response
		}
		System.out.println(request.getContextPath());

		response.setStatus(HttpServletResponse.SC_OK);

	}

	// This is equivalent to logout
	public static void delete(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		session.invalidate();
		response.setStatus(HttpServletResponse.SC_OK);

	}

}