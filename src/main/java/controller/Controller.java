package controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import model.Database;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static int SLEEP_SECONDS = 0;

	private String getResourceFromURI(HttpServletRequest request) {
		String URI = request.getRequestURI();
		return URI.substring(URI.lastIndexOf('/') + 1);
	}

	// Constructor for this servlet, which is only run once (on startup)
	public Controller() {
		// For debugging purposes
		System.out.println("Running Controller constructor");
		System.out.println("Default encoding for this Java installation: " + Charset.defaultCharset().displayName());

		// Establish connection pool to database
		Database.init();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);

		try {
			TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("data".equals(resource)) {
			Data.get(request, response);
		} else if ("session".equals(resource)) {
			Session.get(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);

		try {
			TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("session".equals(resource)) {
			Session.post(request, response);
		} else if("user".equals(resource)) {
			User.post(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);

		try {
			TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("data".equals(resource)) {
			Data.put(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resource = getResourceFromURI(request);

		try {
			TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("session".equals(resource)) {
			Session.delete(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}
	
	
	
	
	

	
	

}
