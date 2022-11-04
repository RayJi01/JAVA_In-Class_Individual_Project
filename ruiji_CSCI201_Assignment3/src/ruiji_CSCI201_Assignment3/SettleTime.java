package ruiji_CSCI201_Assignment3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;  
import java.util.Date;
import java.util.TimeZone;

public class SettleTime {
	private long millisec;
	private Date result;
	private DateFormat simple;
	public SettleTime(long time) {                 //refer to the method that professor provided on Piazza. 
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
	
	public String getCurrentFormat() {  
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		   LocalDateTime now = LocalDateTime.now();
		   return dtf.format(now);
	}
}


 

