package ruiji_cs201_assignment1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ruiji_cs201_assignment1.Datum;

public class Stock_info {
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
			if(data.get(i).getTicker().toLowerCase().equals(n)) {
				return data.get(i);
			}
		}
		
		return null;
	}
	
	public List<String> getStockfromExchange(String n) {
		List<String> exchange_stock = new ArrayList<String>();
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).getExchangeCode().toLowerCase().equals(n)) {
				exchange_stock.add(data.get(i).getName());
			}
		}
		
		return exchange_stock;
	}
	
	public Boolean check_existent(String name) {
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void adding_new_stock(String n, String t, String sd, String d, String e) {
		Datum new_stock = new Datum(n, t, sd, d, e);
		data.add(new_stock);
		return;
	}
	
	public void remove_stock(int n) {
		data.remove(n);
		return;
	}
	
	public void ascendingSort() {
		data.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		return;
	}
	
	public void descendingSort() {
		data.sort((o2, o1) -> o1.getName().compareTo(o2.getName()));
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
}
