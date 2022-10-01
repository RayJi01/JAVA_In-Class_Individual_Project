package ruiji_CSCI201_Assignment2;

import java.util.List;
import java.util.concurrent.Semaphore;
import ruiji_CSCI201_Assignment2.SettleDate;

public class Operations extends Thread {
	private Semaphore semaphore;
	private String ticker;
	private int quantity;
	private List<Integer> current_balance;
	private int stock_price;
	private int getTime;
	private long time;
	private boolean FinalTrans;
	
	public Operations(int Broker_num, String ticker, int quantity, List<Integer> current_balance, int stock_price, int getTime, long time, boolean FinalTrans) {
		this.semaphore = new Semaphore(Broker_num);
		this.ticker = ticker;
		this.quantity = quantity;
		this.current_balance = current_balance;
		this.stock_price = stock_price;
		this.getTime = getTime;
		this.time = time;
		this.FinalTrans = FinalTrans;
	}
	
	public synchronized int setBalance(int amount) {
		current_balance.set(0, current_balance.get(0) - amount);
		return current_balance.get(0);
	}
	
	public void run() {
		try {
			semaphore.acquire();                                                //acquire the lock. 
			
			Thread.sleep(getTime*1000);                                                 //sleep for 2 seconds first before trade                                                                   //set up the right now time. 
			
			//selling processes started, and the thread will sleep for 3 seconds to finish his job. 
			if(quantity < 0) { 
				long current = System.currentTimeMillis();
				SettleDate timestamp = new SettleDate(current - time); 
				if(quantity == -1) System.out.println("[" + timestamp.getTime() + "]" +  "Start to sale of " + quantity * -1 + " stock of " + ticker);
				else
					System.out.println("[" + timestamp.getTime() + "]" + "Start to sale of " + quantity * -1 + " stocks of " + ticker);
				
				//take 3 seconds to complete the job. 
				Thread.sleep(3000);
				int now_balance = setBalance(quantity * stock_price);
				current = System.currentTimeMillis();
				timestamp = new SettleDate(current - time);
				if(quantity == -1) {
					System.out.println("[" + timestamp.getTime() + "]" + "Finish sale of " + quantity * -1 + " stock of " + ticker);
					System.out.println("Current Balance after trade: " + now_balance);
				}
				else {
					System.out.println("[" + timestamp.getTime() + "]" + "Finish sale of " + quantity * -1 + " stocks of " + ticker);
					System.out.println("Current Balance after trade: " + now_balance);
				}
			}
			//Buying processes started, and the thread will sleep for 2 seconds to finish his job. 
			else {                                                             
				long current = System.currentTimeMillis();
				SettleDate timestamp = new SettleDate(current - time);
				if(quantity == 1) System.out.println("[" + timestamp.getTime() + "]" + "Start to purchase of " + quantity + " stock of " + ticker);
				else
					System.out.println("[" + timestamp.getTime() + "]" + "Start to purchase of " + quantity + " stocks of " + ticker);
				
				Thread.sleep(2000);
				if(current_balance.get(0).compareTo(quantity * stock_price) < 0) {           
					//we don't have that much balance, transaction failed
					int now_balance = setBalance(0);
					System.out.println("Transaction failed due to insufficient balance. Unsuccessful purchase\r\n"
							+ "of " + quantity +  " stocks of " + ticker);
					System.out.println("Current Balance after trade: " + now_balance);
				}
				else {
					int now_balance = setBalance(quantity * stock_price);
					current = System.currentTimeMillis();
					timestamp = new SettleDate(current - time);
					if(quantity == 1) {
						System.out.println("[" + timestamp.getTime() + "]" + " Finish buying of " + quantity + " stock of " + ticker);
						System.out.println("Current Balance after trade: " + now_balance);
					}
					else {
						System.out.println("[" + timestamp.getTime() + "]" + "Finish buying of " + quantity + " stocks of " + ticker);
						System.out.println("Current Balance after trade: " + now_balance);
					}
				}
			}
			
			if(FinalTrans == true) {
				System.out.println("All trades completed!");                     //if we reach the final transactions, we shall output that message inside the thread!. 
			}	
		} catch (InterruptedException ie) {
			System.out.println("MyThread.run IE: " + ie.getMessage());
		}finally {
			semaphore.release();
		}
	}
}