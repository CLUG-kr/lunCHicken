package com.clug.lunchicken.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
		this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF_8"), true);
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
	
	public void send(String msg) {
		writer.println(msg);
		writer.flush();
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
