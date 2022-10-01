package ruiji_cs201_assignment1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.*;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Scanner;

public class File_read_in {
	

	public static void main(String []args) {
		Scanner sc = new Scanner(System.in); //place to check whether the file can be opened or not.
		String filename = "";
		String temp = "";
		Gson gson = new Gson();
		boolean error = false;
		do {
			error = false;
			try {
				System.out.println("What is the name of the company file?");
				filename = sc.nextLine();
				File new_file = new File(filename);
				Scanner newFile = new Scanner(new_file);
				while(newFile.hasNext()) {
					temp += newFile.nextLine();
				}
				Stock_info trial = gson.fromJson(temp, Stock_info.class);
				if(trial.check_misising() == true) {
					System.out.println("The file " + filename + " is not formatted properly - missing keys");
					error = true;
				}
			}catch(FileNotFoundException e){
				System.out.println("The file " + filename + " cannot be found.");
				error = true;
			}catch(JsonSyntaxException e) {
				System.out.println("The file " + filename + " is not formatted properly");
				error = true;
			}catch(JsonParseException e) {
				System.out.println("The file " + filename + " is not formatted properly");
				error = true;
			}
		}while(error);
		
		Stock_info stock_list = gson.fromJson(temp, Stock_info.class);
		System.out.println("The file has been properly read.");
		System.out.println(" ");
//		System.out.println(stock_list.getDatabyIndex(0).getTicker());
//		if(stock_list.getDatabyIndex(0).getTicker().equals("TSLA")) {
//			System.out.println("True");
//		}
//		else
//		{
//			System.out.println("False");
//		}
		
		Scanner user_input = new Scanner(System.in);
		
		printDashboard();
		
		Integer choice = null;                                           //check for whether the input is valid, must be integer
		do {
			System.out.println("What would you like to do? ");
			String check = user_input.nextLine();
			try {
				choice = Integer.parseInt(check);
			}catch(NumberFormatException e){
				System.out.println("This is not a valid option!");
			}
		}while(choice == null);
		
		while(choice != 7) {
			if(choice < 1 || choice > 7) {
				System.out.println("This is not a valid options");                  //need to add up all the options tomorrow. 
				System.out.println(" ");
			}
			else if(choice == 1) {
				int length = stock_list.getNumofStock();
				for(int i = 0; i < length; i++) {
					stock_list.getDatabyIndex(i).displayDetail();
					System.out.println(" ");
				}
			}
			else if(choice == 2) {
				System.out.println("What is the ticker of the company you would like to search for?");
				String ticker = user_input.nextLine().toLowerCase();
				while(stock_list.getStockfromTicket(ticker) == null) {
					System.out.println(ticker + " cannot be found, Please try again.");
					System.out.println("What is the ticker of the company you would like to search for?");
					ticker = user_input.nextLine().toLowerCase();
				}
				
				stock_list.getStockfromTicket(ticker).displayShorten();
				System.out.println(" ");
			}
			else if(choice == 3) {
				System.out.println("What is the exchange code of the company you would like to search for?");
				String exchange = user_input.nextLine().toLowerCase();
				while(stock_list.getStockfromExchange(exchange) == null) {
					System.out.println(exchange + " cannot be found, Please try again.");
					System.out.println("What is the exchange code of the company you would like to search for?");
					exchange = user_input.nextLine().toLowerCase();
				}
				
				List<String> output = stock_list.getStockfromExchange(exchange);
				String output_st = "";
				for(int i = 0; i < output.size(); i++) {
					String comma = ", ";
					output_st += output.get(i);
					output_st += comma;
				}
				
				System.out.println(output_st + "are found on the " + exchange.toUpperCase() + " exchange");	
				System.out.println(" ");
			}
			else if(choice == 4) {
				System.out.println("What is the name of the company you would like to add?");
				String add_name = user_input.nextLine();
				while(stock_list.check_existent(add_name).equals(true)) {
					System.out.println(add_name + " is already in the list, Please try again.");
					System.out.println("What is the name of the company you would like to add?");
					add_name = user_input.nextLine();
				}
				
				String add_descr = "";
				System.out.println("What is the stock symbol of " + add_name);
				String add_ticker = user_input.nextLine();
				System.out.println("What is the start date of " + add_name);
				String add_startdate = user_input.nextLine();
				System.out.println("What is the exchange where " + add_name + " is listed?");
				String add_exchange = user_input.nextLine();
				System.out.println("What is the description of " + add_name);
				
				add_descr += user_input.nextLine();
				stock_list.adding_new_stock(add_name, add_ticker, add_startdate, add_descr, add_exchange);
				
				System.out.println("There is now a new entry for:");
				stock_list.at(stock_list.getNumofStock()-1).displayDetail();
				System.out.println(" ");
			}
			else if(choice == 5) {
				int length = stock_list.getNumofStock();
				for(int i = 1; i <= length; i++) {
					System.out.println(i + ") " + stock_list.at(i-1).getName());
				}
				
				Integer user_choice = null;                                          
				do {
					System.out.println("Which company do you like to remove?");
					String check = user_input.nextLine();
					try {
						user_choice = Integer.parseInt(check);
					}catch(NumberFormatException e){
						System.out.println("This is not a valid option!");
					}
				}while(user_choice == null);
				
				while(user_choice < 1 || user_choice > length) {
					System.out.println("The option is out of bound, try again");
					do {
						System.out.println("Which company do you like to remove?");
						String check = user_input.nextLine();
						try {
							user_choice = Integer.parseInt(check);
						}catch(NumberFormatException e){
							System.out.println("This is not a valid option!");
						}
					}while(user_choice == null);
				}
				
				int remove_item = user_choice;
				String remove_name = stock_list.at(remove_item - 1).getName();
				stock_list.remove_stock(remove_item - 1);
				System.out.println(remove_name + " is now removed. ");
				System.out.println(" ");
			}
			else if(choice == 6) {
				System.out.println("1) A to Z");
				System.out.println("2) Z to A");
				
				Integer user_choice = null;                                          
				do {
					System.out.println("Which company do you like to remove?");
					String check = user_input.nextLine();
					try {
						user_choice = Integer.parseInt(check);
					}catch(NumberFormatException e){
						System.out.println("This is not a valid option!");
					}
				}while(user_choice == null);
				
				if(user_choice == 1) {
					stock_list.ascendingSort();
					System.out.println("Your companies are now sorted from in alphabetical order (A-Z)");
				}
				else { 
					stock_list.descendingSort();
					System.out.println("Your companies are now sorted from in alphabetical order (Z-A)");
				}
				
				System.out.println(" ");
			}
			
			printDashboard();
			choice = null;
			do {
				System.out.println("What would you like to do? ");
				String check = user_input.nextLine();
				try {
					choice = Integer.parseInt(check);
				}catch(NumberFormatException e){
					System.out.println("This is not a valid option!");
				}
			}while(choice == null);	
		}
		
		choice = null;
		do {
			System.out.println("Would you like to save your edits?");
			System.out.println("     1) Yes");
			System.out.println("     2) No");
			String check = user_input.nextLine();
			try {
				choice = Integer.parseInt(check);
			}catch(NumberFormatException e){
				System.out.println("This is not a valid option!");
			}
		}while(choice == null);
		
		if(choice == 1) {
			Gson gson_back = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			try (FileWriter writer = new FileWriter(filename)){
				gson_back.toJson(stock_list, writer);
			}catch(IOException e){
				e.printStackTrace();
			}
			System.out.println("Your edits have been saved to " + filename);           //need to output the file back to json. 
			System.out.println("Thank you for using my program!");
			user_input.close();
		}
		else {
			System.out.println("Thank you for using my program!");
			user_input.close();
		}	
		
	}
	
	public static void printDashboard() {
		String option_1 = "       1) Display all public companies.";
		String option_2 = "       2) Search for a stock (by ticker)";
		String option_3 = "       3) Search for all stocks on an exchange";
		String option_4 = "       4) Add a new company/stocks";
		String option_5 = "       5) Remove a company";
		String option_6 = "       6) Sort companies";
		String option_7 = "       7) Exit";
		
		System.out.println(option_1);
		System.out.println(option_2);
		System.out.println(option_3);
		System.out.println(option_4);
		System.out.println(option_5);
		System.out.println(option_6);
		System.out.println(option_7);
	}
}
