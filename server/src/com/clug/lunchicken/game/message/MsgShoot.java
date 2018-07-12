package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;

/**
 * request:
 * {
 * 	"action":"player_shoot",
 * 	"data":{
 * 		"angle":"(angleOfGun)"
 * 	}
 * }<br>
 * response:
 * {
 * 	"action":"player_shoot"
 * 	"data":{
 * 		"damage":"(damage)"
 * 	} 
 * 
 * @author JoMingyu
 *
 */
public class MsgShoot extends Message{

	public MsgShoot(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

}
