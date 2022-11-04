package ruiji_CSCI201_Assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import ruiji_CSCI201_Assignment3.SettleTime;
import java.util.concurrent.locks.*;

import ruiji_CSCI201_Assignment3.ServerProgram;
import ruiji_CSCI201_Assignment3.ClientProgram;

public class TradeThread extends Thread{
	private ServerProgram sp;
	private List<Double> initBalance;
	private boolean busy;
	private long st;
	private List<Transaction_info> Pending_Transaction;
	private List<Double> transaction_price;
 	private double finalBalance;
 	private PrintWriter pw;
 	private BufferedReader br;
	
	public TradeThread(Socket s, ServerProgram sp, double ib) {
		try {
			this.Pending_Transaction = new ArrayList<>();
			this.sp = sp;
			List<Double> init = new ArrayList<Double>(1);                   //use a thread-safe array list to store the balance and update it. 
			init.add(ib);
			this.Pending_Transaction = new ArrayList<Transaction_info>();
			this.transaction_price = new ArrayList<Double>();
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.initBalance = init;
			this.busy = false;
			this.finalBalance = 0.0;
			this.st = 0;
			this.start();
		}catch(IOException ioe){
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}
	
	public void run() {
		while(true) {
			try {
				String done = br.readLine();                 //see if the trades are completed. 
				if(done.equals("true")) {
					busy = false;
				}
			}catch(IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}		
	}
	
	public void sendMessage(String Message) {
		
		pw.println(Message);
		pw.flush();
		
	}
	
	public boolean sufficient_budget(double price, int quantity) {
		if(quantity < 0) return true;
		return this.initBalance.get(0) >= price*quantity;
	}
	
	public boolean BusyStatus() {
		return busy;
	}		
	
	public double gain() {
		return this.finalBalance - this.initBalance.get(0);
	}
	
	public void AssignTrade(HashMap<Transaction_info, Double> bag, long time) {              //we also have to keep track of the time here at the assigning. 
		for(Map.Entry<Transaction_info, Double> mapElement: bag.entrySet()) {
			Transaction_info ti = mapElement.getKey();
			double price = mapElement.getValue();
			long current = System.currentTimeMillis();
			SettleTime timestamp = new SettleTime(current - time);
			int quantity = ti.getHowmany();
			String Name = ti.getName();
			
			if(quantity < 0) sendMessage("[" + timestamp.getTime() + "]" +  "Assigned to sale of " + quantity * -1 + " stock of " + Name + ". Total Gain Estimate: " + price + "*" + quantity*-1 + " = "+ price * quantity* -1);
			else
				sendMessage("[" + timestamp.getTime() + "]" +  "Assigned to purchase of " + quantity + " stock of " + Name + ". Total Cost Estimate: " + price + "*" + quantity + " = "+ price * quantity);
			if(quantity > 0) {
				this.initBalance.set(0, initBalance.get(0) - price*quantity);
			}
			this.Pending_Transaction.add(ti);
			this.transaction_price.add(price);
		}
		
		if(bag.size()>0) {             //if the bag contains objects, we set the busy boolean equals true and then let them run the operations. 
			busy = true;
			sendMessage("S");
			sendMessage(Integer.toString(Pending_Transaction.size()));
			for(int i = 0; i < Pending_Transaction.size(); i++) {
				Transaction_info ti = Pending_Transaction.get(i);
				String Name = ti.getName();
				String quantity = Integer.toString(ti.getHowmany());
				String price = Double.toString(transaction_price.get(i));
				String line = Name + " " + quantity + " " + price + " " + time;
				if(Integer.parseInt(quantity) < 0) { 
					finalBalance += Integer.parseInt(quantity) *-1 *Double.parseDouble(price);
				}
				sendMessage(line);
			}
			sendMessage("E");
			
			while(Pending_Transaction.isEmpty() == false) {
				Pending_Transaction.remove(0);
				transaction_price.remove(0);
			}
		}	
	}
	
	public void Conclusion(ArrayList<String> Incomplete_trade) {
		if(Incomplete_trade.size() > 0) {
			sendMessage("C");
			sendMessage(Integer.toString(Incomplete_trade.size()));
			for(int i = 0; i < Incomplete_trade.size(); i++) {
				sendMessage(Incomplete_trade.get(i));
			}
			sendMessage("FB");
			sendMessage(Double.toString(finalBalance));
			return;
		}
		
		sendMessage("NC");
		sendMessage("Incomplete_Trades:NONE" + " " + Double.toString(finalBalance));
	}
}


