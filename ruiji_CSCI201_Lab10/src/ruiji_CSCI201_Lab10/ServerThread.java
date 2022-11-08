package ruiji_CSCI201_Lab10;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class ServerThread extends Thread{
	// to do --> private variables for the server thread 
	private Server sr;
	private PrintWriter pw;
	private BufferedReader br;
	private String HTML;
	private String ReturnType;
	private HttpServletResponse response;
	private PrintStream psi;
	
	
	public ServerThread(Socket s, Server sr) {
		try {
			// to do --> store them somewhere, you will need them later 
			this.psi = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
			
			// to do --> complete the implementation for the constructor
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			HTML = "";
			this.sr = sr;
			this.start();		
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}

	// to do --> what method are we missing? Implement the missing method 
	
	public void run() {
			try {
				String requestLine = br.readLine();
				System.out.println(requestLine);
				if(requestLine.substring(0,3).equals("GET")) {
					String[] arrofStr = requestLine.split(" ", 0);
					HTML = arrofStr[1].substring(1);
				}
				try {
					File newfile = new File(this.HTML);
					BufferedReader br2 = new BufferedReader(new FileReader(newfile));
					psi.print("HTTP/1,1 200 OK\r\n");
//						psi.flush();
					psi.print("COntent-Type: text/html; charset=UTF-8\r\n\r\n");
//						psi.flush();
					String temp = "";
					while((temp = br2.readLine()) != null) {
						psi.print(temp);
					}
					psi.flush();
					
				}catch(FileNotFoundException e) {
					psi.print("HTTP/1.1 404 Not Found\r\n");
					psi.print("COntent-Type: text/html; charset=UTF-8\r\n\r\n");
					BufferedReader br2 = new BufferedReader(new FileReader("notFound.html"));
					String temp = "";
					while((temp = br2.readLine()) != null) {
						psi.print(temp);
					}
					psi.flush();
				}
				
			
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
