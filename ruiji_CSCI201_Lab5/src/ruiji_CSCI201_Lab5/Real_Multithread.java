package ruiji_CSCI201_Lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

public class Real_Multithread extends Thread{
	private List<Integer> storage;
	private int search_num;
	private static int result;
	private int start;
	private int end;
	private long start_date;
	private long end_date;
	
	public Real_Multithread(List<Integer> input_list, int search_num, int start, int end) {
		this.storage = input_list;
		this.search_num = search_num;
		this.start = start;
		this.end = end;
	}
	
	public void run() {
		this.start_date = System.nanoTime();
		for(int i = this.start; i < end; i++) {
			if(storage.get(i).equals(search_num)) {
				long time_elapsed = 0;
				this.end_date = System.nanoTime();
				time_elapsed = this.end_date - this.start_date;
				System.out.println("Find the items from multithread with + " + i + time_elapsed + " time. ");
				result = i;
			}
		}
		
		this.end_date = System.nanoTime();
		result = -1;
	}
	
	
	public static int getResult() {
		return result;
	}
}
