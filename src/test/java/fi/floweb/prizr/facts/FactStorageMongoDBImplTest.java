package fi.floweb.prizr.facts;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fi.floweb.prizr.beans.MultiplierBase;

public class FactStorageMongoDBImplTest {

	@Test
	public void testInstantiates() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl("localhost", "testcollection");
		assertNotNull(dao);
		dao.clearFacts();
		assertEquals(0,dao.getFacts().size());
	}
	
	@Test
	public void testClearFactsEmptiesFacts() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		assertNotNull(dao);
		MultiplierBase newFact = new MultiplierBase();
		dao.storeFact(newFact);
		dao.clearFacts();
		assertEquals(0,dao.getFacts().size());
	}
	
	@Test
	public void testDeleteFactDeletesInsertedFact() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
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
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		dao.clearFacts();
		dao.storeFact(fact);
		MultiplierBase readFact = dao.getFacts().get(0);
		assertTrue(readFact.equals(fact));
		dao.clearFacts();
	}
	
	@Test
	public void testIncompleteDBObjectWorksInBeanConversion() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		DBObject db = new BasicDBObject();
		db.put("_id", new ObjectId("563887c4d4c6a5481e2eccf3"));
		db.put("appliesToCategory", "testcategory");
		db.put("multiplier", new Double(1.24));
		db.put("isDomestic", true);
		db.put("countryCode", "testcountrycode");
		db.put("baseFreightMultiplier", new Double(5.67));
		MultiplierBase incomplete = dao.DBObjectToMultiplierBase(db);
		assertNotNull(incomplete);
		assertTrue(incomplete.getAppliesToCategory().equals("testcategory"));
		assertEquals(incomplete.getMultiplier(), new Double(1.24).doubleValue(),2);
		assertEquals(incomplete.isDomestic(), true);
		assertTrue(incomplete.getCountryCode().equals("testcountrycode"));
		assertEquals(incomplete.getBaseFreightMultiplier(), new Double(5.67).doubleValue(), 2);
		assertTrue(incomplete.getAppliesToLocation().equals(""));
		assertTrue(incomplete.getAppliesToShopCode().equals(""));
		assertEquals(incomplete.getIncludesFreight(), new Double(0).doubleValue(), 2);
	}
	
	@Test(expected = IllegalStateException.class)
	public void objectWithoutIDThrowsIllegalState() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		DBObject db = new BasicDBObject();
		db.put("appliesToCategory", "testcategory");
		db.put("multiplier", new Double(1.24));
		db.put("isDomestic", true);
		db.put("countryCode", "testcountrycode");
		db.put("baseFreightMultiplier", new Double(5.67));
		MultiplierBase incomplete = dao.DBObjectToMultiplierBase(db);
		assertNull(incomplete);
	}
	
	@Test
	public void testObjectUpdateWithJustInsertedObject() {
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
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		dao.clearFacts();
		dao.storeFact(fact);
		ArrayList<MultiplierBase> stored = dao.getFacts();
		String updateId = stored.get(0).getId();
		MultiplierBase updateFact = stored.get(0);
		updateFact.setAppliesToCategory("updatetestcategory");
		updateFact.setAppliesToLocation("updatetestlocation");
		updateFact.setAppliesToShopCode("updatetestshopcode");
		updateFact.setBaseFreightMultiplier(11.23);
		updateFact.setCountryCode("updatetestcountrycode");
		updateFact.setDomestic(false);
		updateFact.setFreightMultiplier(14.56);
		updateFact.setIncludesFreight(17.89);
		updateFact.setMultiplier(19.99);
		updateFact.setMultiplierBaseDescription("updatetestmultiplierbasedescription");
		updateFact.setMultiplierBaseName("updatetestmultiplierbasename");
		dao.updateFact(updateId, updateFact);
		ArrayList<MultiplierBase> storedAfterUpdate = dao.getFacts();
		assertEquals(1, storedAfterUpdate.size());
		MultiplierBase readFact = storedAfterUpdate.get(0);
		assertEquals(updateFact.getAppliesToCategory(), readFact.getAppliesToCategory());
		assertEquals(updateFact.getAppliesToLocation(), readFact.getAppliesToLocation());
		assertEquals(updateFact.getAppliesToShopCode(), readFact.getAppliesToShopCode());
		assertEquals(updateFact.getBaseFreightMultiplier(), readFact.getBaseFreightMultiplier(),2);
		assertEquals(updateFact.getCountryCode(), readFact.getCountryCode());
		assertEquals(updateFact.isDomestic(), readFact.isDomestic());
		assertEquals(updateFact.getFreightMultiplier(), readFact.getFreightMultiplier(),2);
		assertEquals(updateFact.getIncludesFreight(), readFact.getIncludesFreight(),2);
		assertEquals(updateFact.getMultiplier(), readFact.getMultiplier(),2);
		assertEquals(updateFact.getMultiplierBaseDescription(), readFact.getMultiplierBaseDescription());
		assertEquals(updateFact.getMultiplierBaseName(), readFact.getMultiplierBaseName());
	}
	
	@Test
	public void testObjectUpdateFailsWithBrokenObjectId() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		assertFalse(dao.updateFact("BrokenObjectId", new MultiplierBase()));
	}

	@Test
	public void testObjectUpdateFailsWithNonExistentObjectId() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl( "localhost", "testcollection");
		assertFalse(dao.updateFact("5690e272fffff9875ac08df2", new MultiplierBase()));
	}	
}
