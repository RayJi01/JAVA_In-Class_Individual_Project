package ruiji_CSCI201_Lab4;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SleepingBarber extends Thread {

	private static int maxSeats;                               //shared value must be static, so that it can statically be the given amount. 
	private boolean isSleeping;
	private static int totalCustomers;
	private String barberName;
	private static ArrayList<Customer> customersWaiting;
	private Lock barberLock;
	private Condition sleepingCondition;
	private static boolean moreCustomers;
	public SleepingBarber(String bn) {
		maxSeats = 3;
		barberName = bn;
		totalCustomers = 10;
		moreCustomers = true;
		customersWaiting = new ArrayList<Customer>();
		barberLock = new ReentrantLock();
		sleepingCondition = barberLock.newCondition();
		this.start();
	}
	
	public boolean isSleeping() {
		return this.isSleeping;
	}
	
	public String getBNName() {
		return barberName;
	}
	public static synchronized boolean addCustomerToWaiting(Customer customer) {
		if (customersWaiting.size() == maxSeats) {
			return false;
		}
		Util.printMessage("Customer " + customer.getCustomerName() + " is waiting");
		customersWaiting.add(customer);
		String customersString = "";
		for (int i=0; i < customersWaiting.size(); i++) {
			customersString += customersWaiting.get(i).getCustomerName();
			if (i < customersWaiting.size() - 1) {
				customersString += ",";
			}
		}
		Util.printMessage("Customers currently waiting: " + customersString);
		return true;
	}
	public void wakeUpBarber() {
		try {
			barberLock.lock();
			sleepingCondition.signal();
		} finally {
			barberLock.unlock();
		}
	}
	public void run() {
		while(moreCustomers) {
			while(!customersWaiting.isEmpty()) {
				Customer customer = null;
				synchronized(this) {
					customer = customersWaiting.remove(0);
				}
				customer.startingHaircut();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					System.out.println(this.getBNName() + " is cutting customer's hair" + ie.getMessage());
				}
				customer.finishingHaircut();
				Util.printMessage(this.getBNName() + " is Checking for more customers...");		
			}
			try {
				barberLock.lock();
				isSleeping = true;
				Util.printMessage("No customers for " + this.getBNName() + ". time to sleep...");
				sleepingCondition.await();
				isSleeping = false;
				Util.printMessage("Someone woke me up!");
			} catch (InterruptedException ie) {
				System.out.println("ie while sleeping: " + ie.getMessage());
			} finally {
				barberLock.unlock();
			}
		}
		Util.printMessage("All done for today!  Time to go home!");
		
	}
	public static void main(String [] args) {
		SleepingBarber sb = new SleepingBarber("Barber1");
		SleepingBarber sb2 = new SleepingBarber("Barber2");
		ExecutorService executors = Executors.newCachedThreadPool();
		for (int i=0; i < SleepingBarber.totalCustomers; i++) {
			Customer customer = new Customer(i, sb, sb2);
			executors.execute(customer);
			try {
				Random rand = new Random();
				int timeBetweenCustomers = rand.nextInt(2000);
				Thread.sleep(timeBetweenCustomers);
			} catch (InterruptedException ie) {
				System.out.println("ie in customers entering: " + ie.getMessage());
			}
		}
		executors.shutdown();
		while(!executors.isTerminated()) {
			Thread.yield();
		}
		Util.printMessage("No more customers coming today...");
		SleepingBarber.moreCustomers = false;
		sb.wakeUpBarber();
	}
}

