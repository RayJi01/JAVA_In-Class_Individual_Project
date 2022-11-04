package ruiji_CSCI201_Assignment3;

public class Transaction_info {
	private int when;
	private String StockName;
	private int howmany;
	
	public Transaction_info(String when, String StockName, String howmany) {
		this.when = Integer.parseInt(when);
		this.StockName = StockName;
		this.howmany = Integer.parseInt(howmany);
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
}
