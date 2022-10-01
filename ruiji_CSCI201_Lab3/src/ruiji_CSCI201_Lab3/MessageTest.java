package ruiji_CSCI201_Lab3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageTest {
	public static void main(String[] args) {
		
		for(int i = 0; i < 2; i++) {
			MessageQueue q = new MessageQueue();
			Messenger m = new Messenger(q);
			Subscriber s = new Subscriber(q);
			
			ExecutorService excu = Executors.newFixedThreadPool(20);
			excu.execute(m);
			excu.execute(s);
			excu.shutdown();
			
			while(!excu.isTerminated()) {
				Thread.yield();
			}
		}	
	}
	
}
