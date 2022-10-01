package ruiji_CSCI201_Assignment2;

public class Transaction_info {
	private int when;
	private String StockName;
	private int howmany;
	private int price;
	
	public Transaction_info(String when, String StockName, String howmany, String price) {
		this.when = Integer.parseInt(when);
		this.StockName = StockName;
		this.howmany = Integer.parseInt(howmany);
		this.price = Integer.parseInt(price);
	}
	
	public int getTime() {
		return this.when;
	}
	
	public String getName() {
		return this.StockName;
	}
	
	public int getHowmany() {
		return this.howmany;
	}
	
	public int getPrice() {
		return this.price;
	}
}
