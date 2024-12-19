package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletHolder;


public class Main {
    public static void main(String[] args) throws Exception {

        // Create Jetty server on port 7070
        Server server = new Server(7070);

        // Create handler
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        handler.setContextPath("/");  // TODO: Is the example.com/path/ ?
        handler.setBaseResourceAsString("src/main/resources/public");

        // Create defaultServlet for serving static resources
        ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        defaultServlet.setInitParameter("welcomeFiles", "index.html");
        defaultServlet.setInitParameter("dirAllowed", "false");

        // Add servlets to handler
        handler.addServlet(defaultServlet, "/*");
        handler.addServlet(ApiServlet.class, "/api/*");

        // Set handler for server and start server
        server.setHandler(handler);
        server.start();
        server.join();

    }
}