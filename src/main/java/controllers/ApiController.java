package controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Database;

/**
 * Controller for handling API endpoints "/api/*"
 */
public class ApiController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Get API resource from request. I.e. what comes after "/api/" in request URI.
	 * 
	 * @param request The HttpServletRequest object
	 * @return API resource
	 */
	private String getApiResource(HttpServletRequest request) {

		String URI = request.getRequestURI();

		int index = URI.indexOf("/api/");
		if (index == -1) {
			return "";
		}

		return URI.substring(index + 5);

	}

	// Constructor for this servlet, which is only run once (on startup)
	public ApiController() {
		// For debugging purposes
		System.out.println("Running Api Controller constructor");
		System.out.println("Default encoding for this Java installation: " + Charset.defaultCharset().displayName());

		// Establish connection pool to database
		Database.init();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getApiResource(request);

		if ("data".equals(resource)) {
			Data.get(request, response);
			return;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getApiResource(request);

		if ("session".equals(resource)) {
			Session.post(request, response);
			return;
		}

		if ("user".equals(resource)) {
			User.post(request, response);
			return;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getApiResource(request);

		if ("data".equals(resource)) {
			Data.put(request, response);
			return;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getApiResource(request);

		if ("session".equals(resource)) {
			Session.delete(request, response);
			return;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

	}

}
