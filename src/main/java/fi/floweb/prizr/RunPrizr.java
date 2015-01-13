package fi.floweb.prizr;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import fi.floweb.prizr.beans.MultiplierBase;
import fi.floweb.prizr.beans.PricingRequest;
import fi.floweb.prizr.beans.PricingResponse;

/**
 * This is a sample class to launch a rule.
 */
public class RunPrizr {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

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
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
