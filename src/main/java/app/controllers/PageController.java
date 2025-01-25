package app.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import app.entities.User;

public class PageController {

    public static void addRoutes(Javalin app) {
        app.get("/", ctx -> ctx.redirect("plan"));
        app.get("plan", ctx -> showPage("plan", ctx));
        app.get("login", ctx -> showPage("login", ctx));
        app.get("create-account", ctx -> showPage("create-account", ctx));
        app.get("account-menu", ctx -> showPage("account-menu", ctx));
        app.get("change-username", ctx -> showPage("change-username", ctx));
        app.get("change-password", ctx -> showPage("change-password", ctx));
        app.get("delete-account", ctx -> showPage("delete-account", ctx));
    }

    private static void showPage(String path, Context ctx) {

        // Allow any page to be shown if user is logged in
        User activeUser = ctx.sessionAttribute("activeUser");
        if (activeUser != null) {
            ctx.render(path);
            return;
        }

        // "login" and "create-account" are also allowed to be shown if no user is logged in
        if ("login".equals(path) || "create-account".equals(path)) {
            ctx.render(path);
            return;
        }

        ctx.redirect("login");

    }

}