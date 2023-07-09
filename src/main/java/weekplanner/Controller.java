package weekplanner;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// Define data source (connection pool) for Resource Injection
	@Resource(name = "jdbc/WeekPlannerDB")
	public static DataSource connectionPool;

	private String getRequestType(HttpServletRequest request) {
		String URI = request.getRequestURI();
		return URI.substring(URI.lastIndexOf('/') + 1);
	}

	public Controller() {
		// For debugging purposes
		System.out.println("Running Controller constructor");
		System.out.println("Default encoding for this Java installation: " + Charset.defaultCharset().displayName());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestType = getRequestType(request);

		if ("".equals(requestType)) {
			Helper.setFrontPage(request, response);
		} else if ("loaddata".equals(requestType)) {
			Helper.loadData(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestType = getRequestType(request);

		if ("login".equals(requestType)) {
			Helper.login(request, response);
		} else if ("logout".equals(requestType)) {
			Helper.logout(request, response);
		}

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestType = getRequestType(request);

		if ("savedata".equals(requestType)) {
			Helper.saveData(request, response);
		}

	}

}
