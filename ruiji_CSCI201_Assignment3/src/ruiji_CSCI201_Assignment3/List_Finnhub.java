package ruiji_CSCI201_Assignment3;

import java.util.*;

import ruiji_CSCI201_Assignment3.Finnhub_Parsing;

public class List_Finnhub {
	private List<Finnhub_Parsing> data = null;
	
	public void setData(List<Finnhub_Parsing> data) {
		this.data = data;
	}
	
	public Finnhub_Parsing get(int n) {
		return data.get(n);
	}
	
	public int size() {
		return data.size();
	}
	
}
