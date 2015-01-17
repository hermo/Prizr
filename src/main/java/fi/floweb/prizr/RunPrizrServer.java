package fi.floweb.prizr;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.kie.api.runtime.KieSession;

import fi.floweb.prizr.rest.DroolsServletContextClass;

public class RunPrizrServer {
	
	public static KieSession kSession = null;

	public static void main(String[] args) {
		System.out.println("Initializing Jersey/Jetty server...");
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addEventListener(new DroolsServletContextClass());
        
        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);
 
        ServletHolder jerseyServlet = context.addServlet(
             org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
 
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.classnames",
           fi.floweb.prizr.rest.Pricing.class.getCanonicalName());
                
        System.out.println("Init done.");
        try {
        	System.out.println("Starting...");
        	jettyServer.start();
        	System.out.println("Started at port 8080. Joining...");
            jettyServer.join();
            System.out.println("Joined.");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            jettyServer.destroy();
        }
    }

}


