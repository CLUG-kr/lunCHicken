package com.clug.lunchicken.login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.clug.lunchicken.login.account.AccountManager;
import com.clug.lunchicken.login.db.DataBase;

public class LoginServer {
	
	private Logger logger = Logger.getLogger("LoginServer");
	private DataBase database; public DataBase getDatabase() {return database;}
	private ClientHandler clientHandler; public ClientHandler getClientHandler() {return clientHandler;}
	private AccountManager accountManager; public AccountManager getAccountManager() {return accountManager;}
	
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
			logger.info("init database");
			clientHandler = new ClientHandler(this);
			logger.info("init client handler");
			accountManager = new AccountManager(this);
			logger.info("init account manager");
			serverSocket = new ServerSocket(getPort());
			logger.info("init server socket");
			return true;
		} catch (SQLException e) {
			logger.warning("fail to init database");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("something wrong to server socket");
			e.printStackTrace();
		}
		return false;
	}
	
	public void openServer() {
		logger.info("start to open server");
		while (!serverSocket.isClosed()) {
			try {
				logger.info("wait connection");
				Socket client = serverSocket.accept();
				logger.info(client.getInetAddress().toString() + "is connected");
				clientHandler.registerClient(client);
				logger.info(client.getInetAddress().toString() + "is registered");
			} catch (IOException e) {
				logger.warning("something wrong to socket");
				e.printStackTrace();
			}
		}
		System.out.println("Exit");
	}
	
}
