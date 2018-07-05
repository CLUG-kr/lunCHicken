package com.clug.lunchicken.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	
	public GameServer(int port) {
		this.setPort(port);
	}
	
	private int port;
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	
	private ServerSocket serverSocket;
	private ClientHandler clientHandler;
	
	public void initServer() {
		try {
			serverSocket = new ServerSocket(getPort());
			clientHandler = new ClientHandler(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openServer() {
		while (!serverSocket.isClosed()) {
			try {
				Socket client = serverSocket.accept();
				clientHandler.registerClient(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Exit");
	}
	
	public static GameServer server;
	public static void main(String[] data) {
		
		server = new GameServer(7878);
		server.initServer();
		server.openServer();
		
		
	}
}
