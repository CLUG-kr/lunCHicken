package com.clug.lunchicken.login;

import org.json.simple.JSONObject;

public abstract class Message {
	
	protected final LoginServer loginServer;
	public Message(LoginServer loginServer) {
		this.loginServer = loginServer;
	}
	
	public abstract String handleMessage(JSONObject data);
	
}
