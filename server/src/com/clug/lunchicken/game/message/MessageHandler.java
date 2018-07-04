package com.clug.lunchicken.game.message;

import java.util.HashMap;

import com.clug.lunchicken.game.GameServer;


public class MessageHandler {
	private HashMap<String, Message> messageMap;
	private GameServer gameServer;
	public MessageHandler(GameServer gameServer) {
		this.gameServer = gameServer;
		messageMap = new HashMap<>();
		initMessageHandler();
	}
	
	private void initMessageHandler() {
	
	}
	
	public Message getMessageHandler(String action) {
		if (messageMap.containsKey(action)) {
			return messageMap.get(action);
		}
		return null;
	}
}
