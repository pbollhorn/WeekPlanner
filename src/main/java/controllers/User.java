package controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Database;

public class User {

	public static void post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("POST user");

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

		// Use Gson to deserialize the JSON into a Java credentials object
		Credentials credentials = new Gson().fromJson(jsonObject, Credentials.class);

		// Check if username is in database already
		boolean status = Database.checkUsername(credentials);

		// If status == true, respond with status code 409 (CONFLICT)
		if( status == true) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return;	
		}
		
		Database.createUser(credentials);
		
		// WE JUST ASSUME EVERYTHING IS SUCCESSFULL

	}

}
