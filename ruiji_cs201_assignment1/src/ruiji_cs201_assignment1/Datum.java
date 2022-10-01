package ruiji_cs201_assignment1;

import java.util.Collections;

import org.apache.commons.*;
import org.apache.commons.text.WordUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
	private String name;
	private String ticker;
	private String startDate;
	private String description;
	private String exchangeCode;
	
	public Datum(String n, String t, String sd, String d, String e) {
		this.name = n;
		this.ticker = t;
		this.startDate = sd;
		this.description = d;
		this.exchangeCode = e;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
	
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
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
