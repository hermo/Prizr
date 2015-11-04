package prizr;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import fi.floweb.prizr.beans.MultiplierBase;
import fi.floweb.prizr.facts.FactStorageMongoDBImpl;

public class FactStorageMongoDBImplTest {

	@Test
	public void testInstantiates() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl("testcollection");
		assertNotNull(dao);
		dao.clearFacts();
		assertEquals(0,dao.getFacts().size());
	}
	
	@Test
	public void testClearFactsEmptiesFacts() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl("testcollection");
		assertNotNull(dao);
		MultiplierBase newFact = new MultiplierBase();
		dao.storeFact(newFact);
		dao.clearFacts();
		assertEquals(0,dao.getFacts().size());
	}
	
	@Test
	public void testDeleteFactDeletesInsertedFact() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl("testcollection");
		assertNotNull(dao);
		dao.clearFacts();
		MultiplierBase newFact1 = new MultiplierBase();
		dao.storeFact(newFact1);
		MultiplierBase newFact2 = new MultiplierBase();
		dao.storeFact(newFact2);
		ArrayList<MultiplierBase> facts = dao.getFacts();
		MultiplierBase first = facts.get(0);
		String deleteThisId = first.getId();
		dao.deleteFact(deleteThisId);
		ArrayList<MultiplierBase> factsAfterDelete = dao.getFacts();
		MultiplierBase only = factsAfterDelete.get(0);
		assertTrue(!only.getId().equals(deleteThisId));
		dao.clearFacts();
	}
	
	@Test
	public void allFieldsAreRetainedInStorage() {
		MultiplierBase fact = new MultiplierBase();
		fact.setAppliesToCategory("testcategory");
		fact.setAppliesToLocation("testlocation");
		fact.setAppliesToShopCode("testshopcode");
		fact.setBaseFreightMultiplier(1.23);
		fact.setCountryCode("testcountrycode");
		fact.setDomestic(true);
		fact.setFreightMultiplier(4.56);
		fact.setIncludesFreight(7.89);
		fact.setMultiplier(9.99);
		fact.setMultiplierBaseDescription("testmultiplierbasedescription");
		fact.setMultiplierBaseName("testmultiplierbasename");
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl("testcollection");
		dao.clearFacts();
		dao.storeFact(fact);
		MultiplierBase readFact = dao.getFacts().get(0);
		assertTrue(readFact.equals(fact));
		dao.clearFacts();
	}

}
