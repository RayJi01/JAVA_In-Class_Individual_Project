package ruiji_CSCI201_Assignment3;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import ruiji_CSCI201_Assignment3.TradeThread;

public class ClientProgram extends Thread{
	private PrintWriter pw;
	private BufferedReader br;
	private boolean trading_start;
	private boolean Conclusion_start;
	private boolean no_incomplete;
	private boolean dump_start;
	private int length_of_trade;
	private int length_of_dump;
	private long sttime;
	private boolean fb;
	
	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to SalStocks v2.0!");
		System.out.println("Enter the server hostname:");
		String localhost = sc.next();
		System.out.println("Enter the server port:");
		int port = Integer.parseInt(sc.next());
		
		ClientProgram cp = new ClientProgram(localhost, port);
	}
	
	public ClientProgram(String hostname, int port) {
		try{
			 System.out.println("Trying to connect to " + hostname + ":" + port);
			 Socket s = new Socket(hostname, port);
			 System.out.println("Connected to " + hostname + ":" + port);
			 pw = new PrintWriter(s.getOutputStream());
			 br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			 this.Conclusion_start = false;
			 this.dump_start = false;
			 this.length_of_dump = 0;
			 this.start(); 
		 }catch (IOException ioe) {
			 System.out.println("ioe in ChatClient constructor: " + ioe.getMessage());
		 }
	}
	
	public void run() {
		try {
			while(true) {                           //let the server keep running until the whole program shut down. 
				      //Take every output from the server and print out to the screen. 
				String line = br.readLine();
				if(line.equals("S")) {                                  //listen to the start of the trade
					trading_start = true;
					continue;
				}
				else if(line.equals("E")) {                             //listen to the end of the trade. 
					trading_start = false;
					if(length_of_trade == 0) {
						pw.println("true");
						pw.flush();
					}
					continue;
				}
				else if(line.equals("C") || line.equals("NC")) {                //listen for conclusions and whether it have leftover
					this.Conclusion_start = true;
					if(line.equals("NC")) this.no_incomplete = true;
					else
						this.no_incomplete = false;
					continue;
				}
				else if(line.equals("FB")) {
					this.Conclusion_start = false;
					this.fb = true;   //dump completed, we need to see the final balance. 
					continue;
				}
				else if(this.fb == false && Conclusion_start == false && Character.isDigit(line.charAt(0)) == true) {    //listen for number of trade
					length_of_trade = line.charAt(0) - 48;
					continue;
				}
				else if(Conclusion_start == true && Character.isDigit(line.charAt(0)) == true) {
					length_of_dump = line.charAt(0) - 48;                 //record how many incomplete trade is recorded. 
					continue;
				}
				else if(this.fb == false && trading_start == false && Conclusion_start == false){   //listen to broadcast informations. 
					System.out.println(line);
					
				}
				
				
				if(this.fb == true) {                   //receive the final balance of the incomplete cases
					double gain = Double.parseDouble(line);
					System.out.println("Total Profit Earned: " + gain);
					System.out.println("");
					System.out.println("Process Completed!");
				}
				
				if(trading_start == true && length_of_trade > 0) {     //listen to the info of the trade. 
					String Name = ""; 
					int quantity = 0;
					double price = 0.0;
					long time = 0;
					int col = 0;
					int start = 0;
					for(int i = 0; i < line.length(); i++) {
						if(line.charAt(i) == ' ' && col == 0) {
							Name = line.substring(start, i);
							start = i+1;
							col++;
						}
						else if(line.charAt(i) == ' ' && col == 1) {
							quantity = Integer.parseInt(line.substring(start, i));
							start = i+1;
							col++;
						}
						else if(line.charAt(i) == ' ' && col == 2) {
							price = Double.parseDouble(line.substring(start, i));
							start = i+1;
							col++;
						}
						else if(col == 3) {
							time = Long.parseLong(line.substring(start));
							break;
						}
					}
					
					length_of_trade -= 1;
					try {
						this.sttime = time;
						operation(price, quantity, time, Name);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				else if(Conclusion_start == true && length_of_dump == 0) {
					if(this.no_incomplete == true) {                               //no leftover cases, no incomplete trades. 
						String Message = "";
						double gain = 0.0;
						for(int i = 0; i < line.length(); i++) {
							if(line.charAt(i) == ' ') {
								Message = line.substring(0, i);
								gain = Double.parseDouble(line.substring(i+1));
								break;
							}
						}
						SettleTime timestamp = new SettleTime(System.currentTimeMillis() - sttime);
						System.out.println("[" + timestamp.getTime() + "]" + Message);
						System.out.println("Total Profit Earned: " + gain);
						System.out.println("");
						System.out.println("Process Completed!");
						Conclusion_start = false;
					}
				}
				else if(Conclusion_start == true && length_of_dump > 0){                   //receive the incomplete trade info
					SettleTime timestamp = new SettleTime(System.currentTimeMillis() - sttime);
					System.out.println("[" + timestamp.getTime() + "]" + line);	
					length_of_dump -= 1;
				}
				
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void operation(double price, int quantity, long time, String ticker) throws InterruptedException {    //the main operation function.
		if(quantity < 0) { 
			long current = System.currentTimeMillis();
			SettleTime timestamp = new SettleTime(current - time); 
			if(quantity == -1) System.out.println("[" + timestamp.getTime() + "]" +  "Start to sale of " + quantity * -1 + " stock of " + ticker + ". Total gain Estimate: " + price + "*" + quantity*-1 + " = "+ price * quantity*-1);
			else
				System.out.println("[" + timestamp.getTime() + "]" + "Start to sale of " + quantity * -1 + " stocks of " + ticker + ". Total Gain Estimate: " + price + "*" + quantity*-1 + " = "+ price * quantity*-1);
			
			//take 1 seconds to complete the job. 
			Thread.sleep(1000);
			
			current = System.currentTimeMillis();
			timestamp = new SettleTime(current - time); 
			if(quantity == -1) {
				System.out.println("[" + timestamp.getTime() + "]" + "Finish selling of " + quantity * -1 + " stock of " + ticker  + ". Total Gain: " + price + "*" + quantity*-1 + " = "+ price*quantity*-1);
			}
			else {
				System.out.println("[" + timestamp.getTime() + "]" + "Finish selling of " + quantity * -1 + " stocks of " + ticker  + ". Total Gain: " + price + "*" + quantity*-1 + " = "+ price*quantity*-1);
			}
		}
		//Buying processes started, and the thread will sleep for 2 seconds to finish his job. 
		else {                                                             
			long current = System.currentTimeMillis();
			SettleTime timestamp = new SettleTime(current - time);
			if(quantity == 1) System.out.println("[" + timestamp.getTime() + "]" +  "Start to Purchase of " + quantity  + " stock of " + ticker + ". Total Cost Estimate: " + price + "*" + quantity + " = "+ price * quantity);
			else
				System.out.println("[" + timestamp.getTime() + "]" +  "Start to Purchase of " + quantity + " stocks of " + ticker + ". Total gain Estimate: " + price + "*" + quantity + " = "+ price * quantity);
			
			Thread.sleep(1000);
			
			
			current = System.currentTimeMillis();
			timestamp = new SettleTime(current - time);
			if(quantity == 1) {
				System.out.println("[" + timestamp.getTime() + "]" +  "Finish Purchase of " + quantity  + " stock of " + ticker + ". Total Cost Estimate: " + price + "*" + quantity + " = "+ price * quantity);
			}
			else {
				System.out.println("[" + timestamp.getTime() + "]" +  "Finish Purchase of " + quantity  + " stocks of " + ticker + ". Total Cost Estimate: " + price + "*" + quantity + " = "+ price * quantity);	
			}	
		}		
	}
}
