package ruiji_CSCI201_Lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Mutithread_search extends RecursiveTask<Integer>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Integer> input_list;
	private int search_num;
	private long time_start;
	private long time_end;
	private int start;
	private int end;
	private static int result;
	
	public Mutithread_search(List<Integer> input_list, int search_num, int start, int end) {
		this.input_list = input_list;
		this.search_num = search_num;
		this.start = start;
		this.end = end;
		result = -1;
	}
	
	public static int getResult() {
		return Mutithread_search.result;
	}
	
	public void search(int start, int end) {
		long time_elapsed;
		time_start = System.nanoTime();
		for(int i = start; i < end; i++) {
			if(input_list.get(i).equals(search_num)) {
				time_end = System.nanoTime();
				time_elapsed = time_end - time_start;
				System.out.println("The time used for search by parallel computing is "+ i + time_elapsed);
				result = i;
			}
		}
		
		result = -1;
	}
	
	public Integer compute() {
		Integer k = 0;
		search(start, end);
		if(Mutithread_search.result != -1) return 1;
		return k;
	}
}


	

