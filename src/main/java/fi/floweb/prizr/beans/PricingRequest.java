package fi.floweb.prizr.beans;

public class PricingRequest {

	String locationCode;
	String countryCode;
	String itemCategoryCode;
	double salesPrice;
	double itemStandardCost;
	int itemUnitCC;
	
	public PricingRequest() {	
	}
	
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getItemCategoryCode() {
		return itemCategoryCode;
	}
	public void setItemCategoryCode(String itemCategoryCode) {
		this.itemCategoryCode = itemCategoryCode;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public double getItemStandardCost() {
		return itemStandardCost;
	}
	public void setItemStandardCost(double itemStandardCost) {
		this.itemStandardCost = itemStandardCost;
	}
	public int getItemUnitCC() {
		return itemUnitCC;
	}
	public void setItemUnitCC(int itemUnitCC) {
		this.itemUnitCC = itemUnitCC;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("locationCode: "+locationCode);
		buf.append("countryCode: "+countryCode);
		buf.append("itemCategoryCode: "+itemCategoryCode);
		buf.append("salesPrice: "+salesPrice);
		buf.append("itemStandardCost: "+itemStandardCost);
		buf.append("itemUnitCC: "+itemUnitCC);
		return buf.toString();
	}
	

}
