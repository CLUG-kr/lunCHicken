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

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		boolean chk = true;
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "register_player");
		String accountId = (String) data.get("account_id");
		if (accountId == null) {
			chk = false; // 값이 없을 경우
			resData.put("response", String.valueOf(chk));
			resObj.put("data", resData.toJSONString());
			return resObj.toJSONString();
		}
		gameServer.getGameHandler().registerPlayer(client, accountId);
		chk = true; 
		resData.put("response", String.valueOf(chk));
		resObj.put("data", resData.toJSONString());
		return resObj.toJSONString();
	}

}
