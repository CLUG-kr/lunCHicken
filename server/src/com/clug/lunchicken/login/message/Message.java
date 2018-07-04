package com.clug.lunchicken.login.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.login.LoginServer;

public abstract class Message {
	
	protected final LoginServer loginServer;
	public Message(LoginServer loginServer) {
		this.loginServer = loginServer;
	}
	
	public abstract String handleMessage(JSONObject data);
	
}
