package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.GameServer;

public abstract class Message {
	protected final GameServer gameServer;
	public Message(GameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	public abstract String handleMessage(JSONObject data);
}
