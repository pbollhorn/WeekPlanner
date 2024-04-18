package controller;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Database;

public class ApiController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String getResourceFromURI(HttpServletRequest request) {
		String URI = request.getRequestURI();
		return URI.substring(URI.lastIndexOf('/') + 1);
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

		String resource = getResourceFromURI(request);


		if ("data".equals(resource)) {
			Data.get(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);


		if ("session".equals(resource)) {
			Session.postNEW(request, response);
		} else if ("user".equals(resource)) {
			User.post(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);


		if ("data".equals(resource)) {
			Data.put(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);

		if ("session".equals(resource)) {
			Session.delete(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

}
