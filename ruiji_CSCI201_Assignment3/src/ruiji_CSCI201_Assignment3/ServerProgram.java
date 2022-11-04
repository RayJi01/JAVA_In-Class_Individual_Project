package ruiji_CSCI201_Assignment3;

//--------------------------------Import related classes in the project directory-----------------------------------//

import ruiji_CSCI201_Assignment3.SettleTime;
import ruiji_CSCI201_Assignment3.Transaction_info;
import ruiji_CSCI201_Assignment3.Trader;
import ruiji_CSCI201_Assignment3.Finnhub_Parsing;
import ruiji_CSCI201_Assignment3.List_Finnhub;
import ruiji_CSCI201_Assignment3.TradeThread;

//--------------------------------------------Import related library-----------------------------------------------//

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.File;

import com.google.gson.Gson;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import jdk.dynalink.Operation;

public class ServerProgram {
	private ArrayList<TradeThread> TradeThreads;
	private HashMap<String, Double> ticker_price_map;
	private ArrayList<String> incomplete_trade;
	public ServerProgram(int port){
		this.incomplete_trade = new ArrayList<>();
		this.TradeThreads = new ArrayList<>();
		
		
//---------------------------Read in The transaction info (schedule.csv) from the CSV files.-----------------------//
		
		
		Scanner sc = new Scanner(System.in);
		boolean error = true;
		String filename = "";
		ArrayList<Transaction_info> transact = new ArrayList<>();              //initiate the database for csv file info
		do {
			error = false;
			try {
				System.out.println("What is the path of the schedule file?");
				filename = sc.nextLine();
				File new_CSV = new File(filename);
				Scanner newFile = new Scanner(new_CSV);
				String when = "", StockName = "", howmany = "";
				while(newFile.hasNextLine()) {
					int column = 0;
					String line = newFile.nextLine();
					Scanner rowscanner = new Scanner(line);
					rowscanner.useDelimiter(",");
					while(rowscanner.hasNext()) {
						if(column == 0) when = rowscanner.next();
						else if(column == 1) StockName = rowscanner.next();
						else {
							howmany = rowscanner.next();
							Transaction_info ti = new Transaction_info(when, StockName, howmany);
							transact.add(ti);
							column = 0;
							continue;
						}	
						
						column += 1;
					}
					
				}
			}catch(FileNotFoundException e) {
				System.out.println("The file " + filename + " cannot be found.");
				System.out.println("");
				error = true;
			}
		}while(error);
		
//		System.out.println(transact.get(5).getHowmany() + transact.get(5).getName() + transact.get(5).getTime());   //for testing input. 
		
		System.out.println("The schedule file has been properly read.");
		System.out.println("");
		
//--------------------------------------Read in the trader.csv file.----------------------------------------------//
		
		ArrayList<Trader> TraderList = new ArrayList<>();
		do {
			error = false;
			try {
				System.out.println("What is the path of the traders file?");
				int column = 0;
				filename = sc.nextLine();
				File trader_csv = new File(filename);
				Scanner trader_scan = new Scanner(trader_csv);
				trader_scan.useDelimiter(",|\r\\n");               //delimeter of the csv of each column. 
				
				String trader_num = "", init_balance = "";         //the expected variable of the read in file. 
				while(trader_scan.hasNext()) {
					if(column == 0) trader_num = trader_scan.next();
					else{
						init_balance = trader_scan.next();
						Trader newtrader = new Trader(trader_num, init_balance);
						TraderList.add(newtrader);
						trader_num = "";
						init_balance = "";
						column = 0;
						continue;
					}
					
					column += 1;
				}
			}catch(FileNotFoundException e) {
				System.out.println("The file " + filename + " cannot be found.");
				System.out.println("");
				error = true;
			}
		}while(error);
		
		//System.out.println(TraderList.get(0).getID() + "" + TraderList.get(0).getBalance());    //for testing input
		
		System.out.println("The traders file has been properly read.");
		System.out.println("");
		
		int expected_client_num = TraderList.size();
		
//--------------------------------------Read in from the real time Finnhub API---------------------------------------------------//
		
		ticker_price_map = new HashMap<>();                //extract the distinct ticker out from the schedule.csv
		for(int i = 0; i < transact.size(); i++) {
			if(ticker_price_map.containsKey(transact.get(i))==false) {
				ticker_price_map.put(transact.get(i).getName(), 0.0);
			}
		}
		
		Gson gson = new Gson();
		List_Finnhub ticker_list;
		
		try{
			String API_KEY = "cdfg6gaad3i8a4q92aj0cdfg6gaad3i8a4q92ajg";
			for (Map.Entry<String,Double> mapElement : ticker_price_map.entrySet()) {   //how we iterate through hashmap in java. 
	            String key = mapElement.getKey();
	            String readLine = null;
	            
	            
	            URL url = new URL("https://finnhub.io/api/v1/quote?symbol=" + key + "&token=" + API_KEY);  //Apply HTTP Request and Finnhub API to get the real-time price of stock 
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
			        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			        StringBuffer response = new StringBuffer();
			        while ((readLine = in .readLine()) != null) {
			            response.append(readLine);
			        } in .close();
			        
			        Finnhub_Parsing fp = gson.fromJson(response.toString(), Finnhub_Parsing.class);
			        ticker_price_map.put(key, fp.getCurrentPrice());
			    } else {
			        System.out.println("GET NOT WORKED");
			    }
				
				//System.out.println(key + ":" + mapElement.getValue());
			}	
		}catch(IOException ie) {
			System.out.println("cannot retrieve data from the finnhub!");
		}catch(JsonSyntaxException e) {
			System.out.println("the data retrieve is not in correct format!");
		}catch(JsonParseException e) {
			System.out.println("the data retrieved cannot be parsed correctly!");
		}
		
		System.out.println("");
		
//----------------------------------Constructing the server that is ready to take in client of traders-----------------------------------------//
		
		try {
			System.out.println("Listening on port " + port);
			ServerSocket ss = new ServerSocket(port);              //bind to the port of 3456.
			System.out.println("Bound to port " + port);
			System.out.println("Wait for traders...");
			
			this.TradeThreads = new ArrayList<TradeThread>();
			int p = 0;
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress());
				int remaining_traders = expected_client_num - 1 - p;
				boolean start = false;
				double current_trader_balance = TraderList.get(p).getBalance();
				
				TradeThread tt = new TradeThread(s, this, current_trader_balance);         //tell the client if there are still need to wait.
				TradeThreads.add(tt);
				
				
				if(remaining_traders != 0) {
					System.out.println("Waiting for " + remaining_traders + " more traders");
					this.broadcast(remaining_traders + " traders should be arrived to activate the trading", tt);
				}
				else {
					System.out.println("Starting Service.");
					for(TradeThread threads: TradeThreads) {
						this.broadcast("All Traders arrived", threads);                    //when all the traders arrived, the trade shall be started. 
						this.broadcast("Start Services", threads);
					}
					start = true;
				}
				if(p == expected_client_num) {
					p = 0;
				}
				p++;
				
//--------------------------------------------------------the trading started------------------------------------------------------------------------------------//
				
				long start_time = System.currentTimeMillis();                              //start to count the time of the trading
				if(start == true) {
					int index = 0;
					while(index < transact.size()) {                                          //go over the schedule of the transactions. 
						int index_ori = index;
						long current = System.currentTimeMillis() - start_time;
						HashMap<Transaction_info, Double> Trade_List = new HashMap<>();
						while (current/1000 >= transact.get(index).getTime()) {                       //its the time for trading. 
							String stock_name = transact.get(index).getName();
							double price = this.findPrice(ticker_price_map, stock_name);
							Trade_List.put(transact.get(index), price);
							index++;
							if(index == transact.size()) {
								break;
							}
						}
						
						if(Trade_List.size() > 0) {
							TradeAssign(Trade_List, TradeThreads, start_time);
						}
						
						if(index == transact.size()) {
							break;
						}
					}
					
					for(int i = 0; i < TradeThreads.size(); i++) {
						TradeThreads.get(i).Conclusion(incomplete_trade);
					}
					System.out.println("Process Completed!");
					
				}
			}	
		} catch (IOException ioe) {
			System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());            //section that construct the connection for each client into a server thread. 
		}	
	}
	
	public static void main(String []args) {
		ServerProgram sp = new ServerProgram(3456);
	}
	
//----------------------------------------------------------Supplemented Class needed in the Server Program------------------------------------------------------//	
	
	public void broadcast(String Message, TradeThread tt) {                                   //Server broadcasting to every user for the server. 			
		
		tt.sendMessage(Message);
	}
	
	
	public void TradeAssign(HashMap<Transaction_info, Double> Trade_List, ArrayList<TradeThread> TTs, long st) {
		boolean success = false;
		HashMap<Transaction_info, Double> Trade_bag1 = new HashMap<>();
		HashMap<Transaction_info, Double> Trade_bag2 = new HashMap<>();
		for(Map.Entry<Transaction_info, Double> mapElement: Trade_List.entrySet()) {
		   Transaction_info ti = mapElement.getKey();
		   double price = mapElement.getValue();
		   for(int i = 0; i < TTs.size(); i++) {              //loop through every traders to see if someone can handle
			   TradeThread trader = TTs.get(i);
			   
			   if(ti.getHowmany()> 0) {
				   if(trader.BusyStatus() == false && trader.sufficient_budget(price, ti.getHowmany()) == true) {                      //it can buy the stock. 
					    if(i == 0) Trade_bag1.put(ti, price);
						else {
							Trade_bag2.put(ti, price);
						}
					    success = true;
						break;
					}
				}
				else {
					if(trader.BusyStatus() == false){                    //it can buy the stock. 
						if(i == 0) Trade_bag1.put(ti, price);
						else {
							Trade_bag2.put(ti, price);
						}
						success = true;
						break;
					}
				}
		   }
		   
		   if(success == true) success = false;
		   else
			   incomplete_trade.add(format_incompleteTrade(ti));   
		}
		
		TTs.get(0).AssignTrade(Trade_bag1, st);                      //packet each trade to the traders. 
		TTs.get(1).AssignTrade(Trade_bag2, st);
	}
	
	public double findPrice(HashMap<String, Double> ticker_price_map, String Name) {
		for (Map.Entry<String,Double> mapElement : ticker_price_map.entrySet()) {   //how we iterate through hashmap in java. 
            if(mapElement.getKey().equals(Name)) return mapElement.getValue();
		}
		return 0.0;
	}
	
	public String format_incompleteTrade(Transaction_info ti) {
		SettleTime time = new SettleTime(System.currentTimeMillis());
		String info = "Incomplete Trades: (" + ti.getTime()+ ", " + ti.getName() + ", " + ti.getHowmany() + ", " + time.getCurrentFormat() + "). ";
		return info;
	}
}
