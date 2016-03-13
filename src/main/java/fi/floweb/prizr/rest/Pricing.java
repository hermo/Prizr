package fi.floweb.prizr.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    return "{\"status\": \"OK\"}";
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
	      int rulesFired = kSession.fireAllRules();
	      System.out.println("Rules matched: "+rulesFired);
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
	  String dbName = (String) application.getAttribute("dbName");
	  FactStorage storage = new FactStorageMongoDBImpl(dbName);
	  ArrayList<MultiplierBase> rules = storage.getFacts();
	  MultiplierBase[] res = new MultiplierBase[rules.size()]; 
	  rules.toArray(res);
	  return res;
  }
  
  @Path("/rules")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public MultiplierBase setRule(MultiplierBase rule) {
	  String dbName = (String) application.getAttribute("dbName");
	  FactStorage storage = new FactStorageMongoDBImpl(dbName);
	  storage.storeFact(rule);
	  KieSession kSession = (KieSession) application.getAttribute("ksession");
	  FactHandle handle = kSession.insert(rule);
	  factHandleCache.put(rule.getId(), handle);
	  return rule;
  }
  
  @Path("/rules/{ruleId}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public MultiplierBase updateRule(@PathParam("ruleId") String ruleId, MultiplierBase rule) {
	  String dbName = (String) application.getAttribute("dbName");
	  FactStorage storage = new FactStorageMongoDBImpl(dbName);
	  storage.updateFact(ruleId,rule);
	  KieSession kSession = (KieSession) application.getAttribute("ksession");
	  FactHandle handle = kSession.insert(rule);
	  factHandleCache.put(rule.getId(), handle);
	  return rule;
  }
  
  @Path("/batch")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Map<String, Integer> batchUpdateAllRules(MultiplierBase[] rules) {
	  if(rules == null) {
		  System.out.println("Rules is null...");
		  return null;
	  }
	  HashMap<String, Integer> res = new HashMap<String, Integer>();
	  String dbName = (String) application.getAttribute("dbName");
	  FactStorage storage = new FactStorageMongoDBImpl(dbName);
	  KieSession kSession = (KieSession) application.getAttribute("ksession");	
	  int updated = 0;
	  int revoked = 0;
	  if(rules == null || rules.length == 0) {
		  res.put("updated", updated);
		  res.put("revoked", revoked);
		  res.put("total", factHandleCache.size());
		  return res;
	  }
	  revoked = factHandleCache.size();
	  factHandleCache = new HashMap<String,FactHandle>();
	  storage.clearFacts();
	  Iterator<MultiplierBase> iter = Arrays.asList(rules).iterator();
	  while(iter.hasNext()) {
		  MultiplierBase next = iter.next();
		  storage.storeFact(next);
		  FactHandle handle = kSession.insert(next);
		  factHandleCache.put(next.getId(), handle);
		  updated++;
	  }
	  res.put("updated", updated);
	  res.put("revoked", revoked);
	  res.put("total", factHandleCache.size());
	  return res;
  }
  
  @Path("/rules")
  @DELETE	
  @Produces(MediaType.APPLICATION_JSON)
  public String deleteRule(String ruleId) {
	  String dbName = (String) application.getAttribute("dbName");
	  FactStorage storage = new FactStorageMongoDBImpl(dbName);
	  if(storage.deleteFact(ruleId)) {
		  KieSession kSession = (KieSession) application.getAttribute("ksession");
		  kSession.delete(factHandleCache.get(ruleId));
		  return "OK";
	  } else {
		  return "FAILED";
	  }
  }

  
} 