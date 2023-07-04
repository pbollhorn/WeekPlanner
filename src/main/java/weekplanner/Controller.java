package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Controller() {
		// For debugging purposes
		System.out.println("Running Controller constructor");
		System.out.println("Default encoding for this Java installation: " + Charset.defaultCharset().displayName());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String jsonString = Model.databaseGetData();
		System.out.println(jsonString);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Controller has been called");

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
		String jsonString = new Gson().toJson(jsonObject);

		// Post the read in json data to database
		Model.databasePostData(jsonString);

		return;

	}

}
