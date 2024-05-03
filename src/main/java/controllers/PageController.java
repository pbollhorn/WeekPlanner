package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for handling user-facing URLs (all hardcoded in web.xml):<br>
 * /account<br>
 * /login<br>
 * /menu<br>
 * /plan<br>
 * /trial<br>
 * <br>
 * welcome-file in web.xml is set to "login",<br>
 * but page name for top level GET request is empty string ""
 */
public class PageController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Get page name from request. I.e. "/pagename" in request URI.<br>
	 * 
	 * @param request The HttpServletRequest object
	 * @return The page name
	 */
	private String getPageName(HttpServletRequest request) {
		String URI = request.getRequestURI();
		return URI.substring(URI.lastIndexOf('/') + 1);
	}

	/**
	 * Handle top level GET request by redirecting to either "login" or "plan".
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 */
	private void handleTopLevelGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (Session.getCredentials(request, response) == null) {
			response.sendRedirect("login");
		} else {
			response.sendRedirect("plan");
		}

	}

	/**
	 * Method for handling GET requests.<br>
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 */
	// When "resource" requested, then "resource.html" is displayed
	// this way the user will not see filename ending in browser, which is more modern
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pageName = getPageName(request);
		System.out.println("Resource: " + pageName);

		// Top level GET requests
		if ("".equals(pageName)) {
			handleTopLevelGetRequest(request, response);
			return;
		}

		// If there is no session, user can see trial.html and login.html
		// default to login.html if user requests anything else
		if (Session.getCredentials(request, response) == null) {

			if ("trial".equals(pageName)) {
				request.getRequestDispatcher("html/trial.html").forward(request, response);
				return;
			}

			request.getRequestDispatcher("html/login.html").forward(request, response);
			return;

		}

		// If there is a session, user can see all pages
		request.getRequestDispatcher("html/" + pageName + ".html").forward(request, response);

	}

}
