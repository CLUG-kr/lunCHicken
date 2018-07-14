package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;
import com.clug.lunchicken.game.gameLayer.gameHandler.GameHandler;
/**
 * request:
 * {
 * 	"action":"join_game",
 * 	"data":{
 * 		"game_id":"(gameId)"
 * 	}
 * }<br>
 * response:
 * {
 * 	"action":"join_game",
 * 	"data": {
 * 		"result":"(0 : success, 1... : fail code)"
 * 	}
 * }
 * 
 * @author JoMingyu
 *
 */
public class MsgJoinGame extends Message {

	public MsgJoinGame(GameServer gameServer) {
		super(gameServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		// 있는지 없는 지 확인
		if (!data.containsKey("game_id")) {
			return null;
		}
		
		try {
			Player player = gameServer.getGameHandler().getPlayer(client);
			if (player == null) return null;
			int gameId = Integer.valueOf((String) data.get("game_id"));
			int resultCode = gameServer.getGameHandler().joinGame(player, gameId);
			
			// 방 가입에 성공했을 경우
			if (resultCode == GameHandler.JOIN_SUCCESS) {
				for (Player p : player.getJoinedGame().getAllPlayers()) {
					p.sendReadyRoomMsg();
				}
			}
			
			JSONObject resObj = new JSONObject();
			JSONObject resData = new JSONObject();
			resObj.put("action", "join_game");
			resData.put("result", String.valueOf(resultCode));
			resObj.put("data", resData);
			return resObj.toJSONString();
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
