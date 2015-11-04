package prizr;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.floweb.prizr.facts.FactStorageMongoDBImpl;

public class FactStorageMongoDBImplTest {

	@Test
	public void testInstantiates() {
		FactStorageMongoDBImpl dao = new FactStorageMongoDBImpl("testcollection");
		assertNotNull(dao);
		assertEquals(0,dao.getFacts().size());
	}

}
