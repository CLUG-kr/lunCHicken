package com.clug.lunchicken.login.message;

import java.util.HashMap;

import com.clug.lunchicken.login.LoginServer;

public class MessageHandler {

	private HashMap<String, Message> messageMap;
	private LoginServer loginServer;
	public MessageHandler(LoginServer loginServer) {
		this.loginServer = loginServer;
		messageMap = new HashMap<>();
		initMessageHandler();
	}
	
	private void initMessageHandler() {
		messageMap.put("login", new MessageLogin(loginServer));
		messageMap.put("register", new MessageRegister(loginServer));
	}
	
	public Message getMessageHandler(String action) {
		if (messageMap.containsKey(action)) {
			return messageMap.get(action);
		}
		return null;
	}
	
}
