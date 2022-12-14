package ruiji_CSCI201_Assignment2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SettleDate {
	private long millisec;
	private Date result;
	private DateFormat simple;
	public SettleDate(long time) {                 //refer to the method that professor provided on Piazza. 
		
		millisec = time;
		// Creating date format
		simple = new SimpleDateFormat("HH:mm:ss:SSS");
		simple.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		// Creating date from milliseconds using Date() constructor
		result = new Date(millisec);
		
		//Formatting data to given format. 
	}
	
	public String getTime() {
		return simple.format(result);
	}
}
