package prizr;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import fi.floweb.prizr.beans.MultiplierBase;
import fi.floweb.prizr.beans.PricingRequest;
import fi.floweb.prizr.beans.PricingResponse;

public class PricingRulesTest {

	private static final double DELTA = 1e-15;
	static KieSession kSession = null;
	static KieContainer kContainer = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		KieServices ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
    	kSession = kContainer.newKieSession("ksession-rules");
	}
	
	@Before
	public void setUpBefore() throws Exception {
		kSession.dispose();
		kSession = kContainer.newKieSession("ksession-rules");
		
	}

	@Test
	public void testKieSessionCreated() {
		assertNotNull(kSession);
	}
	
	@Test
	public void testKieSessionEvaluatesEmpty() {
		assertEquals(0,kSession.fireAllRules());
	}

	@Test
	public void testOneBaseRuleMatchesExceptedPricingRequest() {
		MultiplierBase baseRule = new MultiplierBase();
		baseRule.setAppliesToCategory("UNITTEST");
		baseRule.setAppliesToLocation("UNITLOCATION");
		baseRule.setAppliesToShopCode("UNITTESTSHOP");
		baseRule.setCountryCode("UNITCC");
		baseRule.setDomestic(true);
		baseRule.setFreightMultiplier(2);
		baseRule.setIncludesFreight(2);
		baseRule.setMultiplier(2);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(2);
		testReq.setCountryCode("UNITCC");
		testReq.setItemCategoryCode("UNITTEST");
		testReq.setItemStandardCost(4);
		testReq.setItemUnitCC(2);
		testReq.setLocationCode("UNITLOCATION");
		testReq.setSalesPrice(10);
		testReq.setShopCode("UNITTESTSHOP");
		
		 kSession.insert(baseRule);
         kSession.insert(testReq); 
         PricingResponse pres = new PricingResponse();
         FactHandle handle = kSession.insert(pres);
         kSession.fireAllRules();
         PricingResponse response = (PricingResponse) kSession.getObject(handle);
         assertTrue(response.isPricingDone());
         // response should be baseprice + transit where
         // baseprice = salesprice * commission(2) * multiplier(2) = 40
         // transit = (standardcost + (includesFreight / itemUnitcc)) * freightMultiplier
         //i.e. (10 * 2 * 2) + (4 + (2 / 2)) * 2 = 40 + 10 = 50
         assertEquals(50, response.getPrice(), DELTA); 
	}
	
	@Test
	public void testOneBaseRuleWorksOnZeroPriceRequest() {
		MultiplierBase baseRule = new MultiplierBase();
		baseRule.setAppliesToCategory("UNITTEST");
		baseRule.setAppliesToLocation("UNITLOCATION");
		baseRule.setAppliesToShopCode("UNITTESTSHOP");
		baseRule.setCountryCode("UNITCC");
		baseRule.setDomestic(true);
		baseRule.setFreightMultiplier(2);
		baseRule.setIncludesFreight(2);
		baseRule.setMultiplier(2);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(0);
		testReq.setCountryCode("UNITCC");
		testReq.setItemCategoryCode("UNITTEST");
		testReq.setItemStandardCost(0);
		testReq.setItemUnitCC(0);
		testReq.setLocationCode("UNITLOCATION");
		testReq.setSalesPrice(0);
		testReq.setShopCode("UNITTESTSHOP");
		
		 kSession.insert(baseRule);
         kSession.insert(testReq); 
         PricingResponse pres = new PricingResponse();
         FactHandle handle = kSession.insert(pres);
         kSession.fireAllRules();
         PricingResponse response = (PricingResponse) kSession.getObject(handle);
         assertTrue(response.isPricingDone()); 
	}

	@Test
	public void testZeroRuleWorksOnZeroPriceRequest() {
		MultiplierBase baseRule = new MultiplierBase();
		baseRule.setAppliesToCategory("UNITTEST");
		baseRule.setAppliesToLocation("UNITLOCATION");
		baseRule.setAppliesToShopCode("UNITTESTSHOP");
		baseRule.setCountryCode("UNITCC");
		baseRule.setDomestic(true);
		baseRule.setFreightMultiplier(0);
		baseRule.setIncludesFreight(0);
		baseRule.setMultiplier(0);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(0);
		testReq.setCountryCode("UNITCC");
		testReq.setItemCategoryCode("UNITTEST");
		testReq.setItemStandardCost(0);
		testReq.setItemUnitCC(0);
		testReq.setLocationCode("UNITLOCATION");
		testReq.setSalesPrice(0);
		testReq.setShopCode("UNITTESTSHOP");
		
		 kSession.insert(baseRule);
         kSession.insert(testReq); 
         PricingResponse pres = new PricingResponse();
         FactHandle handle = kSession.insert(pres);
         kSession.fireAllRules();
         PricingResponse response = (PricingResponse) kSession.getObject(handle);
         assertTrue(response.isPricingDone()); 
	}

	@Test
	public void testManyRulesMatchingBehavior() {
		MultiplierBase baseRule = new MultiplierBase();
		baseRule.setAppliesToCategory("UNITTEST");
		baseRule.setAppliesToLocation("UNITLOCATION");
		baseRule.setAppliesToShopCode("UNITTESTSHOP");
		baseRule.setCountryCode("UNITCC");
		baseRule.setDomestic(true);
		baseRule.setFreightMultiplier(2);
		baseRule.setIncludesFreight(2);
		baseRule.setMultiplier(2);
		
		MultiplierBase baseRule2 = new MultiplierBase();
		baseRule2.setAppliesToCategory("UNITTEST");
		baseRule2.setAppliesToLocation("UNITLOCATION");
		baseRule2.setAppliesToShopCode("UNITTESTSHOP");
		baseRule2.setCountryCode("UNITCC");
		baseRule2.setDomestic(true);
		baseRule2.setFreightMultiplier(2);
		baseRule2.setIncludesFreight(2);
		baseRule2.setMultiplier(2);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(2);
		testReq.setCountryCode("UNITCC");
		testReq.setItemCategoryCode("UNITTEST");
		testReq.setItemStandardCost(4);
		testReq.setItemUnitCC(2);
		testReq.setLocationCode("UNITLOCATION");
		testReq.setSalesPrice(10);
		testReq.setShopCode("UNITTESTSHOP");
		
		 kSession.insert(baseRule);
		 kSession.insert(baseRule2);
         kSession.insert(testReq); 
         PricingResponse pres = new PricingResponse();
         FactHandle handle = kSession.insert(pres);
         kSession.fireAllRules();
         PricingResponse response = (PricingResponse) kSession.getObject(handle);
         assertTrue(response.isPricingDone());
         // response should be baseprice + transit where
         // baseprice = salesprice * commission(2) * multiplier(2) = 40
         // transit = (standardcost + (includesFreight / itemUnitcc)) * freightMultiplier
         //i.e. (10 * 2 * 2) + (4 + (2 / 2)) * 2 = 40 + 10 = 50
         assertEquals(50, response.getPrice(), DELTA); 
	}
	
}
