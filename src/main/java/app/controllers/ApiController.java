package app.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.persistence.Database;
import app.entities.Credentials;
import app.entities.User;
import app.persistence.ConnectionPool;

public class ApiController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("api/session", ctx -> login(ctx, connectionPool));
        app.post("api/user", ctx -> createUser(ctx, connectionPool));
        app.get("api/data", ctx -> loadData(ctx, connectionPool));
        app.put("api/data", ctx -> saveData(ctx, connectionPool));
    }

    private static void login(Context ctx, ConnectionPool connectionPool) {

        // TODO: Better exception handling using the Jackson exceptions
        try {

            // Use ObjectMapper to parse the incoming JSON body into a Credentials object
            Credentials credentials = new ObjectMapper().readValue(ctx.body(), Credentials.class);

            // Attempt to login
            User activeUser = Database.login(credentials, connectionPool);
            if (activeUser == null) {
                ctx.status(401);
                return;
            }

            ctx.sessionAttribute("activeUser", activeUser);
            ctx.status(200);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private static void createUser(Context ctx, ConnectionPool connectionPool) {

        // TODO: Better exception handling using the Jackson exceptions
        try {

            // Use ObjectMapper to parse the incoming JSON body into a Credentials object
            Credentials credentials = new ObjectMapper().readValue(ctx.body(), Credentials.class);

            if (Database.usernameAvailable(credentials, connectionPool) != 1) {
                ctx.status(409);
                return;
            }

            Database.createUser(credentials, connectionPool);

            // WE JUST ASSUME EVERYTHING IS SUCCESSFULL

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private static void loadData(Context ctx, ConnectionPool connectionPool) {

        User activeUser = ctx.sessionAttribute("activeUser");
        if (activeUser == null) {
            ctx.status(401);
            return;
        }

        String jsonString = Database.loadData(activeUser, connectionPool);
        if (jsonString == null) {
            ctx.status(500);
            return;
        }

        ctx.status(200).result(jsonString);

    }

    private static void saveData(Context ctx, ConnectionPool connectionPool) {

        User activeUser = ctx.sessionAttribute("activeUser");
        if (activeUser == null) {
            ctx.status(401);
            return;
        }

        String jsonString = ctx.body();

        int rowsAffected = Database.saveData(activeUser, jsonString, connectionPool);
        if (rowsAffected != 1) {
            ctx.status(500);
            return;
        }

        ctx.status(200);

    }

}
