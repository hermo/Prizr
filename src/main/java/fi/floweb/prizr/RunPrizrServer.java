package fi.floweb.prizr;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;
import org.kie.api.runtime.KieSession;

import fi.floweb.prizr.rest.CORSFilter;
import fi.floweb.prizr.rest.DroolsServletContextClass;

public class RunPrizrServer {
	
	public static KieSession kSession = null;

	public static void main(String[] args) throws Exception {
	    String dbHost = null;
		String dbName = null;
		Integer port = null;
		try {
		    dbHost = args[0];
			dbName = args[1];
			port = Integer.parseInt(args[2]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("You need to specify three input parameters: [dbhost] [dbname] [port]");
			System.exit(1);
		} catch (NumberFormatException e) {
			System.out.println("Your port designation needs to be an integer, like 8080.");
			System.exit(1);
		}
		System.out.println("Initializing Jersey and Jetty server...");
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addEventListener(new DroolsServletContextClass(dbHost, dbName));
        FilterHolder holder = new FilterHolder();
        holder.setFilter(new CORSFilter());
        context.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
        
        Server jettyServer = new Server(port);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(
             org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
 
        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.classnames",
           fi.floweb.prizr.rest.Pricing.class.getCanonicalName());
        // log verbose
        jerseyServlet.setInitParameter(
        	"jersey.config.server.tracing","ALL");
        jerseyServlet.setInitParameter(
        		"jersey.config.server.tracing.threshold", "VERBOSE");
        
        MOXyJsonProvider moxyJsonProvider = new MOXyJsonProvider();
        moxyJsonProvider.setWrapperAsArrayName(true);
   
        System.out.println("Starting...");
        jettyServer.start();
        System.out.println("Started at port "+port+". Joining...");
        jettyServer.join();
       
    }

}


