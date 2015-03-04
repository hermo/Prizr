package fi.floweb.prizr.beans;

public class PricingResponse {
	
	private double price;
	String articleNum; // "nimike nro"
	String articleCategory; // "nimikekatogoria"
	String country; // "kotimainen, ulkomainen, kotimainen = FI
	String placeCode; //sijaintikoodi 
	boolean pricingDone; // onko hinnoittelu valmis

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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
		builder.append("\nArticle number: "+this.articleNum);
		builder.append("\n Article category:"+this.articleCategory);
		builder.append("\n Country: "+this.country);
		builder.append("\n Place code: "+this.placeCode);
		builder.append("\n Final price: "+this.price);
		builder.append("\n Pricing done: "+this.pricingDone);
		return builder.toString();
	}

}
