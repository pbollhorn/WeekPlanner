package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletHolder;


public class Main {
    public static void main(String[] args) throws Exception {

        // Create Jetty server on port 7070
        Server server = new Server(7070);

        // Create handler for handling servlets
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");  // TODO: Is the example.com/path/ ?

        // Create defaultServlet for serving static resources
        ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        defaultServlet.setInitParameter("dirAllowed", "false"); // Disable directory listing
        defaultServlet.setInitParameter("welcomeFiles", "index.html"); // Default file in directories
        defaultServlet.setInitParameter("resourceBase", "src/main/resources/public"); // Explicitly set resource base for DefaultServlet


        // Add servlets to handler
        handler.addServlet(defaultServlet, "/*");
        handler.addServlet(ApiServlet.class, "/api/*");

        // Set handler for server and start server
        server.setHandler(handler);
        server.start();
        server.join();

    }
}