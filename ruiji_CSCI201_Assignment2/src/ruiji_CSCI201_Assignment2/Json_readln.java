package ruiji_CSCI201_Assignment2;

import java.io.File;
import ruiji_CSCI201_Assignment2.SettleDate;
import ruiji_CSCI201_Assignment2.Stock_Broker;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import ruiji_CSCI201_Assignment2.Transaction_info;

public class Json_readln {
	static Stock_Broker stock_list;
	
	public static void main(String []args) {
		Scanner sc = new Scanner(System.in); //place to check whether the file can be opened or not.
		String filename = "";                
		Gson gson = new Gson();              //scan the Json File. 
		boolean error = false;
		do {
			error = false;
			try {
				System.out.println("What is the name of the file that containing company information?");
				filename = sc.nextLine();
				stock_list = gson.fromJson(new FileReader(filename), Stock_Broker.class);
				if(stock_list.check_misising() == true) {
					System.out.println("The file " + filename + " is not formatted properly - missing keys");
					error = true;
				}
			}catch(FileNotFoundException e){
				System.out.println("The file " + filename + " cannot be found.");
				System.out.println("");
				error = true;
			}catch(JsonSyntaxException e) {
				System.out.println("The file " + filename + " is not formatted properly");
				System.out.println("");
				error = true;
			}catch(JsonParseException e) {
				System.out.println("The file " + filename + " is not formatted properly");
				System.out.println("");
				error = true;
			}
		}while(error);
		
//		Stock_Broker stock_list = gson.fromJson(new FileReader(filename), Stock_Broker.class);
		System.out.println("The file has been properly read.");
		System.out.println(" ");
		
		
		String temp_CSV = "";
		ArrayList<Transaction_info> transact = new ArrayList<>();              //initiate the database for csv file info
		do {
			error = false;
			try {
				System.out.println("What is the name of the file that containing company information?");
				int column = 0;
				filename = sc.nextLine();
				File new_CSV = new File(filename);
				Scanner newFile = new Scanner(new_CSV);
				newFile.useDelimiter(",|\r\\n");                     //that is how we work with CSV file. We seprate the file by ","
				String when = "", StockName = "", howmany = "", price = ""; 
				while(newFile.hasNext()) {
					if(column == 0) {
						when = newFile.next();
					}
					else if(column == 1) {
						StockName = newFile.next();
					}
					else if(column == 2) {
						howmany = newFile.next();
					}
					else {
						price = newFile.next();
						Transaction_info ti = new Transaction_info(when, StockName, howmany, price);
						transact.add(ti);
						when = "";
						StockName = "";
						howmany = "";
						price = "";
						column = 0;
						continue;          //if we reach the last column, we want to update the column back to 0. And we don't want further changes. 
					}
					
					column += 1;
				}
			}catch(FileNotFoundException e) {
				System.out.println("The file " + filename + " cannot be found.");
				System.out.println("");
				error = true;
			}
		}while(error);
		
		error = true;                                  //take in the balance number to see if its true or not. 
		Integer Value_of_Balance = 0;
		while(error == true) {
			System.out.println("What is the initial Balance? ");
			String Balance = sc.nextLine();
			Value_of_Balance = Integer.valueOf(Balance);
			int flag =Integer.valueOf(Value_of_Balance);
			if(flag > 0) error = false;
			else 
				System.out.println("Please Enter a valid balance greater than 0.");
				System.out.println("");
		}
		
		    //set the balance to array to use pass by reference. 
		List<Integer> balance = new ArrayList<Integer>(1);
		balance.add(0);                                        //initialize the array that just store the balance. 
		balance.set(0, Value_of_Balance);    
		
		//begin each operation by creating those threads. 
		ExecutorService current_executor = Executors.newFixedThreadPool(transact.size());
		long start = System.currentTimeMillis();
		try {
			for(int i = 0; i < transact.size(); i++) {
				String tickerName = transact.get(i).getName();
				int Broker_num = stock_list.getStockfromTicket(tickerName).getBroker();
				int quantity = transact.get(i).getHowmany();
				int stock_price = transact.get(i).getPrice();
				int time_of_transaction = transact.get(i).getTime();
				boolean FinalTrans = false;
				if(i == transact.size() - 1) {
					FinalTrans = true;
				}
				current_executor.execute(new Operations(Broker_num, tickerName, quantity, balance, stock_price, time_of_transaction, start, FinalTrans));
			}
		}catch(Exception ie) {
			System.out.println("MyThread.run IE: " + ie.getMessage());
		}finally {
			current_executor.shutdown();
		}		
	}
}
