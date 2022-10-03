package ruiji_CSCI201_Assignment2;

import java.util.List;
import java.util.concurrent.Semaphore;
import ruiji_CSCI201_Assignment2.SettleDate;
import java.util.concurrent.locks.*;

public class Operations extends Thread {
	private Semaphore semaphore;
	private String ticker;
	private int getTime;
	private long time;
	private boolean FinalTrans;
	private BalanceUpdate bu;
	private int quantity_stock;
	
	public Operations(int Broker_num, String ticker, int quantity, List<Integer> current_balance, int stock_price, int getTime, long time, boolean FinalTrans) {
		this.semaphore = new Semaphore(Broker_num);
		this.ticker = ticker;
		this.getTime = getTime;
		this.time = time;
		this.FinalTrans = FinalTrans;
		this.quantity_stock = quantity;
		bu = new BalanceUpdate(current_balance, stock_price, quantity);
	}
	
	public void run() {
		try {
			semaphore.acquire();                                                //acquire the lock. 
			
			Thread.sleep(getTime*1000);                                                 //sleep for 2 seconds first before trade                                                                   //set up the right now time. 
			
			//selling processes started, and the thread will sleep for 3 seconds to finish his job. 
			if(quantity_stock < 0) { 
				long current = System.currentTimeMillis();
				SettleDate timestamp = new SettleDate(current - time); 
				if(quantity_stock == -1) System.out.println("[" + timestamp.getTime() + "]" +  "Start to sale of " + quantity_stock * -1 + " stock of " + ticker);
				else
					System.out.println("[" + timestamp.getTime() + "]" + "Start to sale of " + quantity_stock * -1 + " stocks of " + ticker);
				
				//take 3 seconds to complete the job. 
				Thread.sleep(3000);
				
				current = System.currentTimeMillis();
				timestamp = new SettleDate(current - time);
				//we have to implement the synchronized class inside the if statement cause if statement may cost time that making the overwritten happened. 
				if(quantity_stock == -1) {
					System.out.println("[" + timestamp.getTime() + "]" + " Finish selling of " + quantity_stock * -1 + " stock of " + ticker + "\r\n" + "Current Balance after trade: " + bu.updateBalance());
				}
				else {
					System.out.println("[" + timestamp.getTime() + "]" + " Finish selling of " + quantity_stock * -1 + " stock of " + ticker + "\r\n" + "Current Balance after trade: " + bu.updateBalance());
				}
			}
			//Buying processes started, and the thread will sleep for 2 seconds to finish his job. 
			else {                                                             
				long current = System.currentTimeMillis();
				SettleDate timestamp = new SettleDate(current - time);
				if(quantity_stock == 1) System.out.println("[" + timestamp.getTime() + "]" + "Start to purchase of " + quantity_stock + " stock of " + ticker);
				else
					System.out.println("[" + timestamp.getTime() + "]" + "Start to purchase of " + quantity_stock + " stocks of " + ticker);
				
				Thread.sleep(2000);
				if(bu.canTransact() == false) {           
					//we don't have that much balance, transaction failed
					System.out.println("Transaction failed due to insufficient balance. Unsuccessful purchase\r\n"
							+ "of " + quantity_stock +  " stocks of " + ticker);
					System.out.println("Current Balance after trade: " + bu.getNowBalance());
				}
				else {
					current = System.currentTimeMillis();
					timestamp = new SettleDate(current - time);
					if(quantity_stock == 1) {
						System.out.println("[" + timestamp.getTime() + "]" + " Finish buying of " + quantity_stock + " stock of " + ticker + "\r\n" + "Current Balance after trade: " + bu.updateBalance());
					}
					else {
						System.out.println("[" + timestamp.getTime() + "]" + " Finish buying of " + quantity_stock + " stock of " + ticker + "\r\n" + "Current Balance after trade: " + bu.updateBalance());	
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

class BalanceUpdate{                                     //the class interface where we implement the synchronized monitors. 
	private List<Integer> current_balance;
	private int quantity;
	private int stock_price;
	private Lock lock = new ReentrantLock();
	
	public BalanceUpdate(List<Integer> current_balance, int quantity, int stock_price) {
		this.current_balance = current_balance;
		this.stock_price = stock_price;
		this.quantity = quantity;
	}
	
	public int updateBalance() {
		lock.lock();
		try {
			int update_num = quantity * stock_price;
			current_balance.set(0, current_balance.get(0) - update_num);
			return current_balance.get(0);
		}finally {
			lock.unlock();
		}
			
	}
	
	public boolean canTransact() {
		lock.lock();
		try {
			if(current_balance.get(0).compareTo(quantity * stock_price) < 0) return false;
			return true;
		}finally {
			lock.unlock();
		}
			
	}
	
	public int getNowBalance() {
		lock.lock();
		try {
			return current_balance.get(0);
		}finally {
			lock.unlock();
		}
		
	}
}
