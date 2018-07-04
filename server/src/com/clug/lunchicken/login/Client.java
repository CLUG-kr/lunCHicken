package com.clug.lunchicken.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable{

	private ClientHandler handler;
	private Socket clientSocket;
	private Thread clientThraed;
	private BufferedReader reader;
	private PrintWriter writer;
	public Client(ClientHandler handler, Socket clientSocket) throws IOException {
		this.handler = handler;
		this.clientSocket = clientSocket;
		this.clientThraed = new Thread(this);
		this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.writer = new PrintWriter(clientSocket.getOutputStream());
		this.clientThraed.start();
	}
	
	public Socket getClientSocket() {
		return this.clientSocket;
	}

	public Thread getClientThraed() {
		return clientThraed;
	}
	
	public void disconnect() {
		getClientThraed().interrupt();
		try {
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				String fromClient = reader.readLine();
				System.out.println("from Client:" + fromClient);
				handler.handleMessage(this, fromClient);
			} catch (SocketException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
}
