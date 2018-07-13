package com.clug.lunchicken.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.clug.lunchicken.game.gameLayer.gameHandler.GameHandler;

public class GameServer {
	
	public GameServer(int port) {
		this.setPort(port);
	}
	
	private int port;
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	
	private ServerSocket serverSocket;
	private ClientHandler clientHandler;
	private GameHandler gameHandler; public GameHandler getGameHandler() { return this.gameHandler; }
	
	public void initServer() {
		try {
			serverSocket = new ServerSocket(getPort());
			clientHandler = new ClientHandler(this);
			gameHandler = new GameHandler(this);
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
		
		server = new GameServer(7778);
		server.initServer();
		server.openServer();
		
		
	}
}
