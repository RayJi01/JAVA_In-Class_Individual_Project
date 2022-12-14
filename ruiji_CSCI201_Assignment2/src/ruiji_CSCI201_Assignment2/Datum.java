package ruiji_CSCI201_Assignment2;

import java.util.Collections;

import org.apache.commons.text.WordUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
	private String name;
	private String ticker;
	private String startDate;
	private int stockBrokers;
	private String description;
	private String exchangeCode;
	
//	public Datum(String n, String t, String sd, int stb, String d, String e) {
//		this.name = n;
//		this.ticker = t;
//		this.startDate = sd;
//		this.stock_broker = stb;
//		this.description = d;
//		this.exchangeCode = e;
//	}

	public String getName() {
		return name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public int getBroker() {
		return stockBrokers;
	}
	
	public void setBroker(int stb) {
		this.stockBrokers = stb;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExchangeCode() {
		return exchangeCode;
	}
	
	public void displayDetail() {
		System.out.println(this.getName() + ", symbol " + this.getTicker() + ", started on " + this.getStartDate() + ", listed on " + this.getExchangeCode());
		String wrapped = WordUtils.wrap(this.getDescription(), 70, "\n\t", true);
		System.out.println("\t" + wrapped);
	}
	
	public void displayShorten() {
		System.out.println(this.getName() + ", symbol " + this.getTicker() + ", started on " + this.getStartDate() + ", listed on " + this.getExchangeCode());
	}	
}

