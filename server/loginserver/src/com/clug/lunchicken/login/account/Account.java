package com.clug.lunchicken.login.account;

import com.clug.lunchicken.login.Client;

public class Account {
	private String accountId;
	private int issuedTime;
	private String loginToken;
	private Client client;
	public Account(String loginToken, Client client, String  accountId) {
		this.setLoginToken(loginToken);
		this.setClient(client);
		this.setAccountId(accountId);
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
	public int getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(int issuedTime) {
		this.issuedTime = issuedTime;
	}
	public void addIssuedTime(int amount) {
		this.issuedTime += amount;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
}
