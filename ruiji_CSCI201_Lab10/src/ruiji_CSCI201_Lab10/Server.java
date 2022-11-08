package ruiji_CSCI201_Lab10;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Server {
	// to do --> data structure to hold server threads 
	ArrayList<ServerThread> ServerThreads;
	
	public Server(int port) {
		// to do --> implement your constructor
		try {
			ServerThreads = new ArrayList<ServerThread>();
			ServerSocket ss = new ServerSocket(port);
			while(true) {
				Socket s = ss.accept();
				ServerThread st = new ServerThread(s, this);
				ServerThreads.add(st);	
			}
		}catch(IOException ioe) {
			System.out.println("IOException when trying to connect");
		}	
	}
	
	public static void main(String [] args) {
		// to do --> implement your main()
		Server sr = new Server(1023);
	}
}
