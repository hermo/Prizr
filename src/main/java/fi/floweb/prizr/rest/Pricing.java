package fi.floweb.prizr.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import com.owlike.genson.Genson;

import fi.floweb.prizr.beans.MultiplierBase;
import fi.floweb.prizr.beans.PricingRequest;
import fi.floweb.prizr.beans.PricingResponse;
import fi.floweb.prizr.facts.FactStorage;
import fi.floweb.prizr.facts.FactStorageMongoDBImpl;

@Path("/basic")
public class Pricing {

	@Context
	private ServletContext application;
	// Used to store Facthandles of rules which are saved and deleted.
	// This way we can insert and remove those to/from the engine when needed
	private static HashMap<String, FactHandle> factHandleCache = new HashMap<String,FactHandle>();
	
	
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String sayPlainTextHello() {
    return "OK";
  }
 
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public PricingResponse getPricing(PricingRequest req) {
	  KieSession kSession = (KieSession) application.getAttribute("ksession");
	  if(kSession == null) {
		  return null;
	  } else {
		  System.out.println("Got pricingRequest:");
		  System.out.println(req);
		  FactHandle reqHandle = kSession.insert(req);
		  PricingResponse res = new PricingResponse();
		  FactHandle resHandle = kSession.insert(res);
	      kSession.fireAllRules();
	      PricingResponse response = (PricingResponse) kSession.getObject(resHandle);
	      kSession.delete(resHandle);
	      kSession.delete(reqHandle);
	      System.out.println("Responded with PricingResponse:");
	      System.out.println(response);
	      double resPrice = response.getPrice();
	      BigDecimal bd = new BigDecimal(resPrice);
	      response.setPrice(bd.setScale(2, RoundingMode.HALF_UP).doubleValue());
	      return response;
	  }
  }
  
  @Path("/rules")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public MultiplierBase[] getRules() {
	  FactStorage storage = new FactStorageMongoDBImpl();
	  ArrayList<MultiplierBase> rules = storage.getFacts();
	  MultiplierBase[] res = new MultiplierBase[rules.size()]; 
	  rules.toArray(res);
	  return res;
  }
  
  @Path("/rules")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public MultiplierBase setRule(MultiplierBase rule) {
	  FactStorage storage = new FactStorageMongoDBImpl();
	  storage.storeFact(rule);
	  KieSession kSession = (KieSession) application.getAttribute("ksession");
	  FactHandle handle = kSession.insert(rule);
	  factHandleCache.put(rule.getId(), handle);
	  return rule;
  }
  
  @Path("/rules")
  @DELETE	
  @Produces(MediaType.APPLICATION_JSON)
  public String deleteRule(String ruleId) {
	  FactStorage storage = new FactStorageMongoDBImpl();
	  if(storage.deleteFact(ruleId)) {
		  KieSession kSession = (KieSession) application.getAttribute("ksession");
		  kSession.delete(factHandleCache.get(ruleId));
		  return "OK";
	  } else {
		  return "FAILED";
	  }
  }

  
} 