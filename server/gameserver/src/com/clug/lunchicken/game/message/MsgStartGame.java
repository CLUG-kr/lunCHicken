package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;
import com.clug.lunchicken.game.gameLayer.gameHandler.Game;
/**
 * request:
 * {
 * 	"action":"start_game",
 * 	"data":{}
 * }<br>
 * response:
 * {
 * 	"action":"start_game",
 * 	"data":{
 * 		"response":"(responseCode)"
 * 	}
 * }
 * @author start
 *
 */
public class MsgStartGame extends Message {

	public MsgStartGame(GameServer gameServer) {
		super(gameServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		int responseCode = 0;
		
		// player check
		Player player = gameServer.getGameHandler().getPlayer(client);
		if (player == null) {
			return null;
		}
		
		// is player host?
		Game game = player.getJoinedGame();
		if (game.getHostPlayer() != player) { // no
			responseCode = 1;
		}
		else { // yes
			// start game
			gameServer.getGameHandler().startGame(player);
			
			// send start Msg
			for (Player p : game.getAllPlayers()) {
				p.sendGameStartMsg();
			}
		}
		
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "start_game");
		resData.put("responseCode", String.valueOf(responseCode));
		resObj.put("data", resData.toJSONString());
		return resObj.toJSONString();
	}

}
