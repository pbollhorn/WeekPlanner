package app;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import app.controllers.ApiController;
import app.controllers.PageController;
import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "WeekPlannerDB";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            config.staticFiles.add("/templates");
        }).start(7070);

        ApiController.addRoutes(app, connectionPool);
        PageController.addRoutes(app, connectionPool);
    }
}