package ruiji_CSCI201_Assignment2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ruiji_CSCI201_Assignment2.Datum;

public class Stock_Broker {
	private List<Datum> data = null;

	public List<Datum> getData() {
		return data;
	}

	public void setData(List<Datum> data) {
		this.data = data;
	}
	
	public Datum getDatabyIndex(int n) {
		return data.get(n);
	}
	
	public int getNumofStock() {
		return data.size();
	}
	
	public Datum at(int n) {
		return data.get(n);
	}
	
	public Datum getStockfromTicket(String n) {
		int i = 0;
		for(i = 0; i < data.size(); i++) {
			if(data.get(i).getTicker().toLowerCase().equals(n.toLowerCase())) {
				return data.get(i);
			}
		}
		
		return null;
	}
	
	public int getIndexTicket(String n) {
		int i = 0;
		for(i = 0; i < data.size(); i++) {
			if(data.get(i).getTicker().toLowerCase().equals(n)) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	public boolean check_existent(String name) {
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void remove_stock(int n) {
		data.remove(n);
		return;
	}
	
	public boolean check_misising() {
		for(int i = 0; i < data.size(); i++) {
			Datum temp = data.get(i);
			if(temp.getName() == null || temp.getTicker() == null || temp.getExchangeCode() == null || temp.getStartDate() == null || temp.getDescription() == null) {
				return true;
			}
		}
		
		return false;
	}
	
	public int size(){
		return data.size();
	}
}
