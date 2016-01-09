package fi.floweb.prizr.facts;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import fi.floweb.prizr.beans.MultiplierBase;

public class FactStorageMongoDBImpl implements FactStorage {

	private static final String FACTCOLLECTION = "facts";
	private static final String DBNAME_DEFAULT = "prizr";
	
	private static MongoClient mongoClient = null;
	private static DB db = null;
	private static DBCollection coll = null;
	private static String currentDbName = null;
	
	private static void init() {
		if (mongoClient == null) {
			try {
				mongoClient = new MongoClient( "localhost:27017" );
			} catch (UnknownHostException e) {
				System.exit(1);
			}
			if(currentDbName == null) {
				db = mongoClient.getDB(DBNAME_DEFAULT);
			} else {
				db = mongoClient.getDB(currentDbName);
			}
			coll = db.getCollection(FACTCOLLECTION); 
		}
	}
	
	public FactStorageMongoDBImpl(String dbName) {
		currentDbName = dbName;
	}
	
	@Override
	public ArrayList<MultiplierBase> getFacts() {
		init();
		ArrayList<MultiplierBase> res = new ArrayList<MultiplierBase>();
		DBCursor facts = coll.find();
		while(facts.hasNext()) {
			MultiplierBase fact = DBObjectToMultiplierBase(facts.next());
			res.add(fact);
		}
		return res;
	}

	@Override
	public boolean storeFact(MultiplierBase fact) {
		init();
		DBObject dbfact = multiplierBaseToDBObject(fact);
		WriteResult res = coll.insert(dbfact,WriteConcern.SAFE);
		return true;
	}

	@Override
	public boolean deleteFact(String objectId) {
		init();
		ObjectId _id;
		try {
			_id = new ObjectId(objectId);
		} catch (IllegalArgumentException e) {
			System.out.println("Failed to convert to object ID item: "+objectId);
			return false;
		}
		DBObject toRemove = new BasicDBObject("_id", _id);
		coll.remove(toRemove);
		return true;
	}

    @Override
    public boolean updateFact(String ruleId, MultiplierBase fact) {
        init();
        ObjectId _id;
        try {
        	_id = new ObjectId(ruleId);
        } catch (IllegalArgumentException e) {
			System.out.println("Failed to convert to object ID item: "+ruleId);
			return false;
		}
        DBObject query = new BasicDBObject("_id", _id);
        WriteResult result = coll.update(query, multiplierBaseToDBObject(fact));
        if(result.getN() == 1) {
        	return true;
        }
        return false;
    }

    @Override
	public boolean clearFacts() {
		init();
		try {
			coll.drop();
		} catch (MongoException e) {
			System.out.println("Failed to drop collection "+coll.getName()+", exception: "+e.getMessage());
			return false;
		}
		return true;
	}
	
	
	protected DBObject multiplierBaseToDBObject(MultiplierBase mb) {
		DBObject res = new BasicDBObject();
		res.put("appliesToCategory", mb.getAppliesToCategory());
		res.put("appliesToLocation", mb.getAppliesToLocation());
		res.put("multiplier",mb.getMultiplier());
		res.put("multiplierBaseDescription", mb.getMultiplierBaseDescription());
		res.put("multiplierBaseName", mb.getMultiplierBaseName());
		res.put("appliesToShopCode", mb.getAppliesToShopCode());
		res.put("isDomestic",mb.isDomestic());
		res.put("countryCode", mb.getCountryCode());
		res.put("includesFreight", mb.getIncludesFreight());
		res.put("freightMultiplier", mb.getFreightMultiplier());
		res.put("baseFreightMultiplier", mb.getBaseFreightMultiplier());
		return res;
		
	}

	protected MultiplierBase DBObjectToMultiplierBase(DBObject db) {
		MultiplierBase res = new MultiplierBase();
		if(!db.containsField("_id")) {
			throw new IllegalStateException("Tried to deserialize bean without unique id from DB!");
		}
		ObjectId uniqueId = (ObjectId)db.get("_id");
		res.setId(uniqueId.toString());
		res.setAppliesToCategory(nullSafeGetString(db,"appliesToCategory"));
		res.setAppliesToLocation(nullSafeGetString(db,"appliesToLocation"));
		res.setMultiplier(nullSafeGetDouble(db,"multiplier"));
		res.setMultiplierBaseDescription(nullSafeGetString(db,"multiplierBaseDescription"));
		res.setMultiplierBaseName(nullSafeGetString(db,"multiplierBaseName"));
		res.setAppliesToShopCode(nullSafeGetString(db,"appliesToShopCode"));
		res.setDomestic(nullSafeGetBoolean(db,"isDomestic"));
		res.setCountryCode(nullSafeGetString(db,"countryCode"));
		res.setIncludesFreight(nullSafeGetDouble(db,"includesFreight"));
		res.setFreightMultiplier(nullSafeGetDouble(db,"freightMultiplier"));
		res.setBaseFreightMultiplier(nullSafeGetDouble(db,"baseFreightMultiplier"));
		return res;
	}
	
	private String nullSafeGetString(DBObject db, String fieldName) {
		if(db.containsField(fieldName)) {
			return (String)db.get(fieldName);
		} 
		return "";
	}
	
	private Double nullSafeGetDouble(DBObject db, String fieldName) {
		if(db.containsField(fieldName)) {
			return (Double)db.get(fieldName);
		} 
		return new Double(0);
	}
	
	private Boolean nullSafeGetBoolean(DBObject db, String fieldName) {
		if(db.containsField(fieldName)) {
			return (Boolean)db.get(fieldName);
		} 
		return false;
	}

	
}
