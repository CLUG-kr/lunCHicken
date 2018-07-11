package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;
/**
 * request:
 * {
 * 	"action":"leave_game",
 * 	"data":{}
 * }<br>
 * response: no-response
 * 
 * @author JoMingyu
 *
 */
public class MsgLeaveGame extends Message {

	public MsgLeaveGame(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		Player player = gameServer.getGameHandler().getPlayer(client);
		if (player == null) return null;
		if (player.getJoinedGame() == null) return null;
		gameServer.getGameHandler().leaveGame(player);
		return MessageHandler.NO_RESPONSE;
	}

}
