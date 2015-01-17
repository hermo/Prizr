package fi.floweb.prizr.rest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Context;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import fi.floweb.prizr.beans.MultiplierBase;
import fi.floweb.prizr.beans.PricingRequest;
import fi.floweb.prizr.beans.PricingResponse;

public class DroolsServletContextClass implements ServletContextListener {

	public static KieSession kSession;
	
	@Context
	private static ServletContext application;
	
	@Override
	public void contextDestroyed(ServletContextEvent scr) {
		kSession.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		droolsInit();
		sce.getServletContext().setAttribute("ksession", kSession);
		
	}
	
	public static KieSession getKieSession() {
		return kSession;
	}
	
	public static void droolsInit() {
		// load up the knowledge base
		System.out.println("Drools engine initializing");
        KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	DroolsServletContextClass.kSession = kContainer.newKieSession("ksession-rules");

    	// fact
    	MultiplierBase base = new MultiplierBase();
    	base.setAppliesToCategory("testCategory");
    	base.setAppliesToLocation("testplace");
    	base.setMultiplier(2.5f);
    	
    	MultiplierBase baseAnother = new MultiplierBase();
    	baseAnother.setAppliesToCategory("someOtherCategory");
    	baseAnother.setAppliesToLocation("testplace");
    	baseAnother.setMultiplier(100f);
    	
        // go !
        PricingRequest preq = new PricingRequest();
        preq.setArticleNum("testArticleNum");
        preq.setArticleCategory("testCategory");
        preq.setCountry("testCountry");
        preq.setPlaceCode("testplace");
        preq.setPurchasePrice(10);
        
        
        PricingResponse pres = new PricingResponse();
        kSession.insert(preq);
        FactHandle handle = kSession.insert(pres); 
        kSession.insert(base);
        kSession.fireAllRules();
        PricingResponse response = (PricingResponse) kSession.getObject(handle);
        System.out.println("Responded with PricingResponse:");
        System.out.println(response);
	}

}