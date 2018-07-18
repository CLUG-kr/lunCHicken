package com.clug.lunchicken.login.account;

import com.clug.lunchicken.login.Client;

public class Account {
	
	private String loginToken;
	private Client client;
	public Account(String loginToken, Client client) {
		this.setLoginToken(loginToken);
		this.setClient(client);
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	
	
}
