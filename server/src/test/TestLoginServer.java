package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TestLoginServer {

	
	public static void main(String[] args) {

		Socket socket;
		try {
			
			socket = new Socket("localhost", 7777);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println("hi");
			writer.flush();
			while(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
