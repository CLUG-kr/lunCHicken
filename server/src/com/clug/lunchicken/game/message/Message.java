package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;

public abstract class Message {
	protected final GameServer gameServer;
	public Message(GameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	/**
	 * 
	 * @param client
	 * @param data
	 * @return null 이면 오류, "no-response" 면 응답을 보내지 않음
	 */
	public abstract String handleMessage(Client client, JSONObject data);
}
