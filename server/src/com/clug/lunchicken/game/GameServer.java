package com.clug.lunchicken.game;

import java.io.IOException;
import java.net.ServerSocket;

public class GameServer {
	
	public GameServer(int port) {
		this.setPort(port);
	}
	
	private int port;
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	
	private ServerSocket serverSocket;
	
	public void initServer() {
		try {
			serverSocket = new ServerSocket(getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openServer() {
		
	}
	
	public static void main(String[] data) {
		
		
	}
}
