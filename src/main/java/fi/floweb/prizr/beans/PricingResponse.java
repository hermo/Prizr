package fi.floweb.prizr.beans;

public class PricingResponse {
	
	private float price;
	String articleNum; // "nimike nro"
	String articleCategory; // "nimikekatogoria"
	String country; // "kotimainen, ulkomainen, kotimainen = FI
	String placeCode; //sijaintikoodi, TKT, HKT etc
	boolean pricingDone; // onko hinnoittelu valmis

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

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

	public boolean isPricingDone() {
		return pricingDone;
	}

	public void setPricingDone(boolean pricingDone) {
		this.pricingDone = pricingDone;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Article number: "+this.articleNum);
		builder.append("Article category:"+this.articleCategory);
		builder.append("Country: "+this.country);
		builder.append("Place code: "+this.placeCode);
		builder.append("Final price: "+this.price);
		builder.append("Pricing done: "+this.pricingDone);
		return builder.toString();
	}

}
