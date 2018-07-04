package com.clug.lunchicken.login;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler {

	private LoginServer loginServer;
	private List<Client> clientList;
	
	public ClientHandler(LoginServer loginServer) {
		this.loginServer = loginServer;
		clientList = new LinkedList<>();
	}
	
	public void registerClient(Socket socket) throws IOException {
		System.out.println(socket);
		Client client = new Client(this, socket);
		clientList.add(client);
	}
	
	public void handleMessage(Client client, String fromClient) {
		System.out.println(fromClient);
	}
	
}
