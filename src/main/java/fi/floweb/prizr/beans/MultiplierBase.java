package fi.floweb.prizr.beans;

import java.io.Serializable;

public class MultiplierBase implements Serializable {

	private static final long serialVersionUID = -3304031619236159475L;
	
	String id; // unique id
	String multiplierBaseName;  // hintakertoimen nimi
	String multiplierBaseDescription; // kuvaus mihin hintakerrointa sovelletaan
	String appliesToCategory; // mihin kategoriaan kerrointa sovelletaan.
	String appliesToLocation; // mihin toimipisteeseen mätsää
	String appliesToShopCode; // mihin kauppaan mätsää
	boolean isDomestic; // onko kotimaahan vai ulkomaille
	String countryCode; 
	String shopName; 
	double includesFreight; // amount of freight costs
	double multiplier; // säännön kerroin
	double freightMultiplier; // rahtikerroin
	
	public MultiplierBase() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMultiplierBaseName() {
		return multiplierBaseName;
	}
	public void setMultiplierBaseName(String multiplierBaseName) {
		this.multiplierBaseName = multiplierBaseName;
	}
	public String getMultiplierBaseDescription() {
		return multiplierBaseDescription;
	}
	public void setMultiplierBaseDescription(String multiplierBaseDescription) {
		this.multiplierBaseDescription = multiplierBaseDescription;
	}
	public String getAppliesToCategory() {
		return appliesToCategory;
	}
	public void setAppliesToCategory(String appliesToCategory) {
		this.appliesToCategory = appliesToCategory;
	}
	public String getAppliesToLocation() {
		return appliesToLocation;
	}
	public void setAppliesToLocation(String appliesToLocation) {
		this.appliesToLocation = appliesToLocation;
	}
	public boolean isDomestic() {
		return isDomestic;
	}
	public void setDomestic(boolean isDomestic) {
		this.isDomestic = isDomestic;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getIncludesFreight() {
		return includesFreight;
	}
	public void setIncludesFreight(double includesFreight) {
		this.includesFreight = includesFreight;
	}
	public double getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public double getFreightMultiplier() {
		return freightMultiplier;
	}

	public void setFreightMultiplier(double freightMultiplier) {
		this.freightMultiplier = freightMultiplier;
	}

	public String getAppliesToShopCode() {
		return appliesToShopCode;
	}

	public void setAppliesToShopCode(String appliesToShopCode) {
		this.appliesToShopCode = appliesToShopCode;
	}
	
}
