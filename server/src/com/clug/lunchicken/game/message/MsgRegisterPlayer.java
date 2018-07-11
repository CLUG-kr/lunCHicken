package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;

/**
 * {
 * 	"action":"register_player",
 * 	"data":{
 * 		"account_id":"(id)"
 * 	}
 * }
 * @author JoMingyu
 */
public class MsgRegisterPlayer extends Message {

	public MsgRegisterPlayer(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		String accountId = (String) data.get("account_id");
		if (accountId == null) return null; // 값이 없을 경우
		gameServer.getGameHandler().registerPlayer(client, accountId);
		return MessageHandler.NO_RESPONSE;
	}

}
