package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;

/**
 * request: {
 * 	"action":"player_position",
 * 	"data":{
 * 		"pos_x":"(posX)",
 * 		"pos_y":"(posY)"
 * 	}
 * }<br>
 * response: no-response
 * @author JoMingyu
 */
public class MsgPosition extends Message{

	public MsgPosition(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		// parse posX
		if (!data.containsKey("pos_x")) return null;
		double posX = Double.valueOf((String) data.get("pos_x"));
		// parse posY
		if (!data.containsKey("pos_y")) return null;
		double posY = Double.valueOf((String) data.get("pos_y"));
		// get player object
		Player player = gameServer.getGameHandler().getPlayer(client);
		if (player == null) return null;
		
		player.getLocation().setPosX(posX);
		player.getLocation().setPosY(posY);
		return MessageHandler.NO_RESPONSE;
	}

}
