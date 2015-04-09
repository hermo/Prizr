package fi.floweb.prizr.beans;

public class PricingResponse {
	
	private double price;
	boolean pricingDone = false; // onko hinnoittelu valmis

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public boolean isPricingDone() {
		return pricingDone;
	}

	public void setPricingDone(boolean pricingDone) {
		this.pricingDone = pricingDone;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n Final price: "+this.price);
		builder.append("\n Pricing done: "+this.pricingDone);
		return builder.toString();
	}

}
