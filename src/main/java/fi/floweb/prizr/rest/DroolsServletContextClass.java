package fi.floweb.prizr.rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Context;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import fi.floweb.prizr.beans.MultiplierBase;
import fi.floweb.prizr.facts.FactStorage;
import fi.floweb.prizr.facts.FactStorageMongoDBImpl;

public class DroolsServletContextClass implements ServletContextListener {

	public static KieSession kSession;
	public static FactStorage factStorage;
	private static String dbName;
	
	@Context
	private static ServletContext application;
	
	public DroolsServletContextClass(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public void contextDestroyed(ServletContextEvent scr) {
		kSession.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		factStorageInit();
		droolsInit();
		sce.getServletContext().setAttribute("ksession", kSession);
		sce.getServletContext().setAttribute("dbName", dbName);
	}
	
	private void factStorageInit() {
		System.out.println("Init fact storage...");
		factStorage = new FactStorageMongoDBImpl(dbName);
		System.out.println("OK.");

	}
	
	public static KieSession getKieSession() {
		return kSession;
	}
	
	public static String getDbName() {
		return dbName;
	}
	
	/**
	 * Init the drools engine. Tests with one pricingrequest which throws
	 * an exception if init is not done properly
	 */
	
	public static void droolsInit() {
		// load up the knowledge base
		System.out.println("Drools engine initializing");
        KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	DroolsServletContextClass.kSession = kContainer.newKieSession("ksession-rules");

    	// load rules from DB and insert to store
    	FactStorage storage = new FactStorageMongoDBImpl(dbName);
  	  	ArrayList<MultiplierBase> rules = storage.getFacts();
    	for(MultiplierBase rule : rules) {
    		System.out.println("Insert persisted rule: "+rule.getMultiplierBaseName());
    		kSession.insert(rule);
    	}
	}

}
