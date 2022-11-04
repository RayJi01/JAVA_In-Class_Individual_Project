package ruiji_CSCI201_Assignment3;

public class Trader {
	private int trader_ID;
	private int init_balance;
	
	public Trader(String trader_num, String init_balance) {
		this.trader_ID = Integer.parseInt(trader_num);
		this.init_balance = Integer.parseInt(init_balance);
	}
	
	public int getID() {
		return this.trader_ID;
	}
	
	public int getBalance() {
		return this.init_balance;
	}
}
