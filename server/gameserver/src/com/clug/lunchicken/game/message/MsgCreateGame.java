package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Location;
import com.clug.lunchicken.game.gameLayer.Player;
import com.clug.lunchicken.game.gameLayer.Zone;
import com.clug.lunchicken.game.gameLayer.gameHandler.Game;
/**
 * request:
 * {
 * 	"action":"create_game",
 * 	"data":{
 * 		"game_name":"(gameName)",
 * 		"max_player":"(maxPlayer)",
 * 		"playingZone":{
 * 			"p1_x":"",
 * 			"p1_y":"",
 * 			"p2_x":"",
 * 			"p2_y":""
 * 		},
 * 		"use_safe_zone":"(true or false)",
 * 		"safe_zone_reduce_time":"()",
 * 		"safe_zone_redece_level":"()"
 * 	}
 * }<br>
 * response: 방 정보가 보내질 것임
 * @author JoMingyu
 *
 */
public class MsgCreateGame extends Message {

	public MsgCreateGame(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		Player player = gameServer.getGameHandler().getPlayer(client);
		Game rawGame = new Game();
		String gameName = (String) data.get("game_name");
		int maxPlayer = Integer.valueOf((String) data.get("max_player"));
		JSONObject playingZoneObj = (JSONObject) data.get("playingZone");
		double p1x = Double.valueOf((String) playingZoneObj.get("p1_x"));
		double p1y = Double.valueOf((String) playingZoneObj.get("p1_y"));
		double p2x = Double.valueOf((String) playingZoneObj.get("p2_x"));
		double p2y = Double.valueOf((String) playingZoneObj.get("p2_y"));
		Zone playingZone = new Zone(new Location(p1x, p1y), new Location(p2x, p2y));
		playingZone.adjustLocation();
		boolean useSafeZone = Boolean.getBoolean((String) data.get("use_safe_zone"));
		int safeZoneReduceTime = Integer.valueOf((String) data.get("safe_zone_reduce_time"));
		int safeZoneReduceLevel = Integer.valueOf((String) data.get("safe_zone_reduce_level"));
		rawGame.createGame(gameName, maxPlayer, playingZone, useSafeZone, safeZoneReduceTime, safeZoneReduceLevel);
		gameServer.getGameHandler().createGame(player, rawGame);
		player.sendReadyRoomMsg();
		return MessageHandler.NO_RESPONSE;
	}

}
