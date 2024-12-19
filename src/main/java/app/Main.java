package app;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.ee10.servlet.ServletHandler;

public class Main {
    public static void main(String[] args) throws Exception {

        // Create a Jetty server on port 7070
        Server server = new Server(7070);

        // Create a servlet holder for the DefaultServlet
        ServletHolder holder = new ServletHolder(new DefaultServlet());

        // Create a context handler to manage servlets and static resources
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setBaseResourceAsString("src/main/resources/public"); // path to your static resources



        // Optionally, configure DefaultServlet's parameters:
        holder.setInitParameter("dirAllowed", "false"); // Disable directory listing
        holder.setInitParameter("welcomeFiles", "index.html"); // Default file in directories

        // Map the DefaultServlet to the URL pattern (e.g., serve everything under "/")
        context.addServlet(holder, "/");

        // Add the context to the server
        server.setHandler(context);

        // Start the server
        server.start();
        server.join();

    }
}