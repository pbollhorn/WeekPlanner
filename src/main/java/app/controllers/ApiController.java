package app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import app.persistence.DataMapper;
import app.entities.Credentials;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.exceptions.DatabaseException;
import app.persistence.UserMapper;

public class ApiController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("api/session", ctx -> login(ctx, connectionPool));
        app.delete("api/session", ctx -> logout(ctx));
        app.post("api/user", ctx -> createUser(ctx, connectionPool));
        app.get("api/data", ctx -> loadData(ctx, connectionPool));
        app.put("api/data", ctx -> saveData(ctx, connectionPool));
    }

    private static void login(Context ctx, ConnectionPool connectionPool) {

        Credentials credentials;
        try {
            credentials = new ObjectMapper().readValue(ctx.body(), Credentials.class);
        } catch (JsonProcessingException e) {
            ctx.status(400);
            return;
        }

        try {
            User activeUser = UserMapper.login(credentials, connectionPool);
            if (activeUser == null) {
                ctx.status(401);
                return;
            }
            ctx.sessionAttribute("activeUser", activeUser);
        } catch (DatabaseException e) {
            ctx.status(500);
        }

    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool) {

        // TODO: Better exception handling using the Jackson exceptions
        try {

            // Use ObjectMapper to parse the incoming JSON body into a Credentials object
            Credentials credentials = new ObjectMapper().readValue(ctx.body(), Credentials.class);

            if (UserMapper.isUsernameAvailable(credentials, connectionPool) != 1) {
                ctx.status(409);
                return;
            }

            UserMapper.createUser(credentials, connectionPool);

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

        try {
            String data = DataMapper.loadData(activeUser, connectionPool);
            ctx.result(data);
        } catch (DatabaseException e) {
            ctx.status(500);
        }

    }

    private static void saveData(Context ctx, ConnectionPool connectionPool) {

        User activeUser = ctx.sessionAttribute("activeUser");
        if (activeUser == null) {
            ctx.status(401);
            return;
        }

        String data = ctx.body();

        try {
            DataMapper.saveData(activeUser, data, connectionPool);
        } catch (DatabaseException e) {
            ctx.status(500);
        }

    }

}
