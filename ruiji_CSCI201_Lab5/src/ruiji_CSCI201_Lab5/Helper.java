package ruiji_CSCI201_Lab5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.*;
import ruiji_CSCI201_Lab5.Linear_Search;
import ruiji_CSCI201_Lab5.Mutithread_search;
import ruiji_CSCI201_Lab5.Real_Multithread;

public class Helper {
	public static void main(String args[]) {
		long time_start = 0;
		long time_end = 0;
		
		List<Integer> ls = new ArrayList<Integer>();
		for(int i = 0; i < 10000000; i++) {
			ls.add(i);
		}
		
		Collections.shuffle(ls);
		
		int num_thread = 4;
		int start = 0, end = ls.size()/num_thread;
		
		Random r = new Random();
		int search_number = r.nextInt(10000000-1);
		System.out.println(search_number);
		
		//linear probing:
		Linear_Search search1 = new Linear_Search(ls, search_number);
		search1.search();
		
		//multithread method:
		ExecutorService executor1 = Executors.newFixedThreadPool(4);
		for(int i = 0; i < num_thread; i++) {
			Real_Multithread mt = new Real_Multithread(ls, search_number, start, end);
			start = end;
			end += ls.size()/num_thread;
			executor1.execute(mt);	
		}
		executor1.shutdown();
			
		
		//parallel threading method:
		ForkJoinPool pool = new ForkJoinPool();
		start = 0;
		end = ls.size()/num_thread;
		Mutithread_search prb[] = new Mutithread_search[num_thread];
		
		long total_time_multi = 0;
		for(int i = 0; i < num_thread; i++) {
			prb[i] = new Mutithread_search(ls, search_number, start, end);
			start = end;
			end = ls.size()/num_thread * (i+2);
			pool.execute(prb[i]);
		}
		
		for(int i = 0 ; i < num_thread; i++) {
			if(Mutithread_search.getResult() != -1) {
				continue;
			}
		}	
	}
	
	
}
