package com.clug.lunchicken.login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import com.clug.lunchicken.login.db.DataBase;

public class LoginServer {
	
	private DataBase database; public DataBase getDatabase() {return database;}
	private ClientHandler clientHandler; public ClientHandler getClientHandler() {return clientHandler;}
	
	public LoginServer(int port) {
		this.setPort(port);
	}
	
	private int port;
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	
	private ServerSocket serverSocket;
		
	public boolean initSever() {
		try {
			database = new DataBase("localhost", "3306", "root", "root");
			clientHandler = new ClientHandler(this);
			serverSocket = new ServerSocket(getPort());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
	
}
