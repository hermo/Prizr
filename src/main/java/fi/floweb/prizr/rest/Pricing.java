package fi.floweb.prizr.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import fi.floweb.prizr.beans.PricingRequest;
import fi.floweb.prizr.beans.PricingResponse;

@Path("/basic")
public class Pricing {

	@Context
	private ServletContext application;
	
  // This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
  }

  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public PricingResponse getPricing(PricingRequest req) {
	  KieSession kSession = (KieSession) application.getAttribute("ksession");
	  if(kSession == null) {
		  return null;
	  } else {
		  kSession.insert(req);
		  PricingResponse res = new PricingResponse();
		  FactHandle resHandle = kSession.insert(res);
	      kSession.fireAllRules();
	      PricingResponse response = (PricingResponse) kSession.getObject(resHandle);
	      System.out.println("Responded with PricingResponse:");
	      System.out.println(response);
	      return response;
	  }
  }

} 