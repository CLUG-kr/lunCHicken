package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;

public class MsgSafeZone extends Message {

	public MsgSafeZone(GameServer gameServer) {
		super(gameServer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

}