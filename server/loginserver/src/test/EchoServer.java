package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {

	public static void main(String[] args) {

		EchoServer echoServer = new EchoServer();
		echoServer.startSever();
		
	}

	
	ServerSocket serverSocket;
	public void startSever() {
		try {
			serverSocket = new ServerSocket(17777);
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println(socket.getInetAddress());
				EchoSocket echo = new EchoSocket(socket);
				echo.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
class EchoSocket implements Runnable{

	public Socket socket;
	public Thread thread;
	private BufferedReader reader;
	private PrintWriter writer;
	public EchoSocket(Socket socket) throws UnsupportedEncodingException, IOException {
		this.socket = socket;
		this.thread = new Thread(this);
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
		
	}
	
	public void start() {
		this.thread.start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String fromClient = reader.readLine();
				if (fromClient != null) System.out.println("from Client:" + fromClient);
				writer.println(fromClient);
			} catch(SocketException e) {
				e.printStackTrace();
				break;
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
