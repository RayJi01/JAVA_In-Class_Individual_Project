package ruiji_CSCI201_Lab5;

import java.util.ArrayList;
import java.util.List;
import java.lang.System;

public class Linear_Search {
	private long time_begin;
	private long time_end;
	private List<Integer> input_list;
	private int search_num;
	
	
	public Linear_Search(List<Integer> input_list, int search_num) {
		this.input_list = input_list;
		this.search_num = search_num;
	}
	
	public int search() {
		time_begin = System.nanoTime();
		for(int i = 0; i < input_list.size(); i++) {
			if(input_list.get(i).equals(search_num)) {
				time_end = System.nanoTime();
				long time_elapsed = time_end - time_begin;
				System.out.println("The time used for search by linear_search is " + i + time_elapsed);
				return i;
			}
		}
		
		System.out.println("Cannot find the item");
		return -1;
	}
}
