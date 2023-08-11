package controller;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Database;

public class Data {

	public static void get(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Credentials credentials = Session.getCredentialsFromCookie(request);
		if (credentials == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String jsonString = Database.loadData(credentials);
		if (jsonString == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	public static void put(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Credentials credentials = Session.getCredentialsFromCookie(request);
		if (credentials == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
		String jsonString = new Gson().toJson(jsonObject);

		// Save the read in json data to database
		int rowsAffected = Database.saveData(credentials, jsonString);
		if (rowsAffected != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		// Return status code 200 because everything was succesful.
		response.setStatus(HttpServletResponse.SC_OK);

	}

}
