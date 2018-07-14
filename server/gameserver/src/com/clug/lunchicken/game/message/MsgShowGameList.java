package com.clug.lunchicken.game.message;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.gameHandler.Game;

/**
 * {
 * 	"action":"show_game_list",
 * 	"data":{}
 * }<br>
 * {
 * 	"action":"show_game_list",
 * 	"data":{
 * 		"game_list":
 * 		{
 * 			"game_id":"(game_id)",
 * 			"game_name":"(game_name)",
 * 			"game_current_player":"()",
 * 			"game_max_player":"()",
 * 			"game_playing_zone":{
 * 				"p1_x":"",
 * 				"p1_y":"",
 * 				"p2_x":"",
 * 				"p2_y":""
 * 			}
 * 		},
 * 		{
 * 			...
 * 	}
 * }
 * @author JoMingyu
 */
public class MsgShowGameList extends Message{

	public MsgShowGameList(GameServer gameServer) {
		super(gameServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(Client client, JSONObject data) {
		List<Game> gameList = gameServer.getGameHandler().getGameList();
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "show_game_list");
		
		JSONArray gameArr = new JSONArray();
		for (Game game : gameList) {
			JSONObject gameObj = new JSONObject();
			gameObj.put("game_id", String.valueOf(game.getGameId()));
			gameObj.put("game_name", game.getGameName());
			gameObj.put("game_current_player", String.valueOf(game.getCurrentPlayer()));
			gameObj.put("game_max_player", String.valueOf(game.getMaxPlayer()));
			JSONObject playingZoneObj = new JSONObject();
			playingZoneObj.put("p1_x", String.valueOf(game.getPlayingZong().getLocation1().getPosX()));
			playingZoneObj.put("p1_y", String.valueOf(game.getPlayingZong().getLocation1().getPosY()));
			playingZoneObj.put("p2_x", String.valueOf(game.getPlayingZong().getLocation2().getPosX()));
			playingZoneObj.put("p2_y", String.valueOf(game.getPlayingZong().getLocation2().getPosY()));
			gameObj.put("game_playing_zone", playingZoneObj);
			gameArr.add(gameObj);
		}
		resData.put("game_list", gameArr.toJSONString());
		resObj.put("data", resData.toJSONString());
		return null;
	}

}
