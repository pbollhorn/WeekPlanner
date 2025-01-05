package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class PageController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

        app.get("/", ctx -> showPage("plan", ctx, connectionPool));
        app.get("account", ctx -> showPage("account", ctx, connectionPool));
        app.get("login", ctx -> showPage("login", ctx, connectionPool));
        app.get("menu", ctx -> showPage("menu", ctx, connectionPool));
        app.get("plan", ctx -> showPage("plan", ctx, connectionPool));
        app.get("trial", ctx -> showPage("trial", ctx, connectionPool));

    }

    private static void showPage(String path, Context ctx, ConnectionPool connectionPool) {

        ctx.render(path);

    }

}