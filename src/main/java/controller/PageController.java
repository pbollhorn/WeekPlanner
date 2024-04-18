package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PageController
 */

public class PageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String getResourceFromURI(HttpServletRequest request) {
		String URI = request.getRequestURI();
		return URI.substring(URI.lastIndexOf('/') + 1);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PageController() {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("Running Page Controller constructor");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
		String resource = getResourceFromURI(request);
		System.out.println("Resource: " + resource);

		// For handling top level GET request
		// (Even though welcome file is set to "view",
		// the URL resource which this servlet gets from top level GET request is "")
		if ("".equals(resource)) {
			// If session cookie is not present, redirect to "login", else redirect to "view"
			if (Session.checkAndRenewSessionNEW(request, response) == 0) {
				response.sendRedirect("login");
			} else {
				response.sendRedirect("view");
			}
		}
		
		// For handling page request to user facing URLs
		// When "resource" requested, then "resource.html" is displayed
		// this way the user will not see filename ending in browser, which is more modern
		else {
			
			
			if("login".equals(resource))
				request.getRequestDispatcher("login.html").forward(request, response);
			else if("trial".equals(resource))
				request.getRequestDispatcher("trial.html").forward(request, response);
			else if (Session.checkAndRenewSessionNEW(request, response) == 0) {
				request.getRequestDispatcher("login.html").forward(request, response);
			} else {
				// PROBABLY NOT VERY SECURE
				// SHOULD RATHER HAVE SWITHC STATEMENT FOR EACH OF THE SPECIFIC .HTML FILES
				request.getRequestDispatcher(resource + ".html").forward(request, response);
			}

		}

	}

}
