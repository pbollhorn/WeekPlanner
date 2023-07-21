package weekplanner;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Helper {

	// checkCredentials request is received from index.html, which is loaded when
	// user performs a top level GET request,
	// i.e. when the user types pbollhorn.dk/weekplanner in their browser.
	public static void checkCredentials(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Authenticator.getCredentialsFromCookie(request) == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	public static void loadData(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Credentials credentials = Authenticator.getCredentialsFromCookie(request);
		if (credentials == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String jsonString = Model.loadData(credentials);
		if (jsonString == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	public static void saveData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Credentials credentials = Authenticator.getCredentialsFromCookie(request);
		if (credentials == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
		String jsonString = new Gson().toJson(jsonObject);

		// Save the read in json data to database
		int rowsAffected = Model.saveData(credentials, jsonString);
		if (rowsAffected != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		// Return status code 200 because everything was succesful.
		response.setStatus(HttpServletResponse.SC_OK);

	}

}
