package fi.floweb.prizr;

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

/**
 * These tests encode problematic test cases which we have seen in production to
 * debug these and catch potential later regressions
 * @author juha
 *
 */

public class RealUseCaseRegressionsTest {

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
	
	/**
	 * Test that pricing rule matches if "Kaikki" ("all" in finnish" is selected as
	 * shop name rule criteria)
	 */
	
	@Test
	public void testMatchingOfShopCodeWildcardKaikki() {
		MultiplierBase allRule = new MultiplierBase();
		
		allRule.setAppliesToCategory("01LEIK-K");
		allRule.setAppliesToLocation("HKT");
		allRule.setMultiplier(2.5);
		allRule.setMultiplierBaseDescription("not a relevant field");
		allRule.setMultiplierBaseName("testi 151102");
		allRule.setAppliesToShopCode("Kaikki");
		allRule.setDomestic(false);
		allRule.setCountryCode("NL");
		allRule.setIncludesFreight(20);
		allRule.setFreightMultiplier(1.2);
		allRule.setBaseFreightMultiplier(2.6);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(1.00);
		testReq.setCountryCode("NL");
		testReq.setItemCategoryCode("01LEIK-K");
		testReq.setItemStandardCost(0.29545);
		testReq.setItemUnitCC(440);
		testReq.setLocationCode("HKT");
		testReq.setSalesPrice(0.3);
		testReq.setShopCode("HKT");
		
		kSession.insert(allRule);
        kSession.insert(testReq); 
        PricingResponse pres = new PricingResponse();
        FactHandle handle = kSession.insert(pres);
        kSession.fireAllRules();
        PricingResponse response = (PricingResponse) kSession.getObject(handle);
		assertTrue(response.isPricingDone());
		assertEquals(response.getPrice(), 1.1590854545454545, DELTA);
	}

	/**
	 * Test that pricing rule matches if "Kaikki" ("all" in finnish" is selected as
	 * shop name rule criteria)
	 */
	
	@Test
	public void testMatchingOfShopCodeWildcardKaikkiWhenOverlappingMoreLocalRule() {
		MultiplierBase allRule = new MultiplierBase();
		MultiplierBase smallRule = new MultiplierBase();
		
		allRule.setAppliesToCategory("01LEIK-K");
		allRule.setAppliesToLocation("HKT");
		allRule.setMultiplier(2.5);
		allRule.setMultiplierBaseDescription("not a relevant field");
		allRule.setMultiplierBaseName("testi 151102");
		allRule.setAppliesToShopCode("Kaikki");
		allRule.setDomestic(false);
		allRule.setCountryCode("NL");
		allRule.setIncludesFreight(20);
		allRule.setFreightMultiplier(1.2);
		allRule.setBaseFreightMultiplier(2.6);
		
		smallRule.setAppliesToCategory("01LEIK-K");
		smallRule.setAppliesToLocation("HKT");
		smallRule.setMultiplier(2.5);
		smallRule.setMultiplierBaseDescription("not a relevant field");
		smallRule.setMultiplierBaseName("testi 151102");
		smallRule.setAppliesToShopCode("HKT");
		smallRule.setDomestic(false);
		smallRule.setCountryCode("NL");
		smallRule.setIncludesFreight(20);
		smallRule.setFreightMultiplier(1.2);
		smallRule.setBaseFreightMultiplier(2.6);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(1.00);
		testReq.setCountryCode("NL");
		testReq.setItemCategoryCode("01LEIK-K");
		testReq.setItemStandardCost(0.29545);
		testReq.setItemUnitCC(440);
		testReq.setLocationCode("HKT");
		testReq.setSalesPrice(0.3);
		testReq.setShopCode("HKT");
		
		kSession.insert(allRule);
		kSession.insert(smallRule);
        kSession.insert(testReq); 
        PricingResponse pres = new PricingResponse();
        FactHandle handle = kSession.insert(pres);
        kSession.fireAllRules();
        PricingResponse response = (PricingResponse) kSession.getObject(handle);
		assertTrue(response.isPricingDone());
		assertEquals(response.getPrice(), 1.1590854545454545, DELTA);
	}
	
	@Test
	public void testMatchingOfCategoryCodeWildcardKaikki() {
		MultiplierBase allRule = new MultiplierBase();
		
		allRule.setAppliesToCategory("Kaikki");
		allRule.setAppliesToLocation("HKT");
		allRule.setMultiplier(2.5);
		allRule.setMultiplierBaseDescription("not a relevant field");
		allRule.setMultiplierBaseName("testi 151102");
		allRule.setAppliesToShopCode("HKT");
		allRule.setDomestic(false);
		allRule.setCountryCode("NL");
		allRule.setIncludesFreight(20);
		allRule.setFreightMultiplier(1.2);
		allRule.setBaseFreightMultiplier(2.6);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(1.00);
		testReq.setCountryCode("NL");
		testReq.setItemCategoryCode("01LEIK-K");
		testReq.setItemStandardCost(0.29545);
		testReq.setItemUnitCC(440);
		testReq.setLocationCode("HKT");
		testReq.setSalesPrice(0.3);
		testReq.setShopCode("HKT");
		
		kSession.insert(allRule);
        kSession.insert(testReq); 
        PricingResponse pres = new PricingResponse();
        FactHandle handle = kSession.insert(pres);
        kSession.fireAllRules();
        PricingResponse response = (PricingResponse) kSession.getObject(handle);
		assertTrue(response.isPricingDone());
		assertEquals(response.getPrice(), 1.1590854545454545, DELTA);
	}
	
	@Test
	public void testMatchingOfLocationWildcardKaikki() {
		MultiplierBase allRule = new MultiplierBase();
		
		allRule.setAppliesToCategory("01LEIK-K");
		allRule.setAppliesToLocation("Kaikki");
		allRule.setMultiplier(2.5);
		allRule.setMultiplierBaseDescription("not a relevant field");
		allRule.setMultiplierBaseName("testi 151102");
		allRule.setAppliesToShopCode("HKT");
		allRule.setDomestic(false);
		allRule.setCountryCode("NL");
		allRule.setIncludesFreight(20);
		allRule.setFreightMultiplier(1.2);
		allRule.setBaseFreightMultiplier(2.6);
		
		PricingRequest testReq = new PricingRequest();
		testReq.setCommission(1.00);
		testReq.setCountryCode("NL");
		testReq.setItemCategoryCode("01LEIK-K");
		testReq.setItemStandardCost(0.29545);
		testReq.setItemUnitCC(440);
		testReq.setLocationCode("HKT");
		testReq.setSalesPrice(0.3);
		testReq.setShopCode("HKT");
		
		kSession.insert(allRule);
        kSession.insert(testReq); 
        PricingResponse pres = new PricingResponse();
        FactHandle handle = kSession.insert(pres);
        kSession.fireAllRules();
        PricingResponse response = (PricingResponse) kSession.getObject(handle);
		assertTrue(response.isPricingDone());
		assertEquals(response.getPrice(), 1.1590854545454545, DELTA);
	}
	
	@Test
	public void testHKTDoesNotMatchSKTInPricing() {
		PricingRequest req1 = new PricingRequest();
		req1.setCommission(8.00);
		req1.setCountryCode("NL");
		req1.setItemCategoryCode("01LEIK-K");	 
		req1.setItemStandardCost(0.37143);	
		req1.setItemUnitCC(350);
		req1.setLocationCode("SKT");
		req1.setSalesPrice(100.00);
		req1.setShopCode("SKT");
		
		MultiplierBase rule1 = new MultiplierBase();		
		rule1.setAppliesToCategory("20RU-K");
		rule1.setAppliesToLocation("HKT");
		rule1.setMultiplier(2);
		rule1.setMultiplierBaseDescription("hkt ruk-k ulk");
		rule1.setMultiplierBaseName("hkt ruk-k");
		rule1.setAppliesToShopCode("Kaikki");
		rule1.setDomestic(false);
		rule1.setCountryCode("-");
		rule1.setIncludesFreight(20);
		rule1.setFreightMultiplier(1.2);
		rule1.setBaseFreightMultiplier(1.2);
		
		MultiplierBase rule2 = new MultiplierBase();
		rule2.setAppliesToCategory("01LEIK-K");
		rule2.setAppliesToLocation("HKT");
		rule2.setMultiplier(2);
		rule2.setMultiplierBaseDescription("hkt leik-k ulk");
		rule2.setMultiplierBaseName("hkt leik-k");
		rule2.setAppliesToShopCode("Kaikki");
		rule2.setDomestic(false);
		rule2.setCountryCode("-");
		rule2.setIncludesFreight(20);
		rule2.setFreightMultiplier(1.2);
		rule2.setBaseFreightMultiplier(1.2);
		
		kSession.insert(rule1);
		kSession.insert(rule2);
		kSession.insert(req1); 
        PricingResponse pres = new PricingResponse();
        FactHandle handle = kSession.insert(pres);
        kSession.fireAllRules();
        PricingResponse response = (PricingResponse) kSession.getObject(handle);
		assertFalse(response.isPricingDone());
	}
	
}
