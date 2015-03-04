package fi.floweb.prizr.beans;

import com.owlike.genson.Genson;

public class PricingRequest {

	String articleNum; // "nimike nro"
	String articleCategory; // "nimikekatogoria"
	String country; // "kotimainen, ulkomainen, kotimainen = FI
	String placeCode; //sijaintikoodi, 
	float cargoPerUnit; // rahtihinta per kpl
	double purchasePrice; // ostohinta, viimeisin yksikkökustannus
	
	public String getArticleNum() {
		return articleNum;
	}
	public void setArticleNum(String articleNum) {
		this.articleNum = articleNum;
	}
	public String getArticleCategory() {
		return articleCategory;
	}
	public void setArticleCategory(String articleCategory) {
		this.articleCategory = articleCategory;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public float getCargoPerUnit() {
		return cargoPerUnit;
	}
	public void setCargoPerUnit(float cargoPerUnit) {
		this.cargoPerUnit = cargoPerUnit;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public String toString() {
		return new Genson().serialize(this);
	}

}
