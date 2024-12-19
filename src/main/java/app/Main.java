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

        // Create a ServletContextHandler to map the servlet
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Add the SimpleServlet to the context
        context.addServlet(new ServletHolder(new MyServlet()), "/");

        // Add the DefaultServlet to serve static resources (optional)
        context.addServlet(DefaultServlet.class, "/static/*");

        // Set the context handler for the server
        server.setHandler(context);

        // Start the server
        server.start();
        server.join();
    }
}