package app.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import app.persistence.ConnectionPool;
import app.entities.User;

public class PageController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> showPage("plan", ctx, connectionPool));
        app.get("plan", ctx -> showPage("plan", ctx, connectionPool));
        app.get("login", ctx -> showPage("login", ctx, connectionPool));
        app.get("trial", ctx -> showPage("trial", ctx, connectionPool));
        app.get("account", ctx -> showPage("account", ctx, connectionPool));
        app.get("menu", ctx -> showPage("menu", ctx, connectionPool));
    }

    private static void showPage(String path, Context ctx, ConnectionPool connectionPool) {

        // Allow any page to be shown if user is logged in
        User activeUser = ctx.sessionAttribute("activeUser");
        if (activeUser != null) {
            ctx.render(path);
            return;
        }

        // "login" and "trial" are also allowed to be shown if no user is logged in
        if ("login".equals(path) || "trial".equals(path)) {
            ctx.render(path);
            return;
        }

        ctx.redirect("login");

    }

}