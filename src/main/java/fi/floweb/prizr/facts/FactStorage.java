package fi.floweb.prizr.facts;

import java.util.ArrayList;

import fi.floweb.prizr.beans.MultiplierBase;

public interface FactStorage {

	ArrayList<MultiplierBase> getFacts();
	boolean storeFact(MultiplierBase fact);
	boolean updateFact(String ruleId, MultiplierBase fact);
	boolean deleteFact(String baseName);
	boolean clearFacts();
	
}
