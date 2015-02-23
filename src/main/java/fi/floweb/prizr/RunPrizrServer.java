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
		System.out.println("Initializing Jersey/Jetty server...");
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addEventListener(new DroolsServletContextClass());
        FilterHolder holder = new FilterHolder();
        holder.setFilter(new CORSFilter());
        context.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
        
        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(
             org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
 
        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.classnames",
           fi.floweb.prizr.rest.Pricing.class.getCanonicalName());
   
        MOXyJsonProvider moxyJsonProvider = new MOXyJsonProvider();
        moxyJsonProvider.setWrapperAsArrayName(true);
   
        System.out.println("Starting...");
        jettyServer.start();
        System.out.println("Started at port 8080. Joining...");
        jettyServer.join();
       
    }

}


