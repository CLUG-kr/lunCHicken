package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;

/**
 * request:
 * {
 * 	"action":"player_shoot",
 * 	"data":{
 * 		"body_part":"(body_part)",
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

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		// parse body part
		if (!data.containsKey("body_part")) return null;
		String bodyPart = (String) data.get("body_part");
		// parse angle of gun
		if (!data.containsKey("angle")) return null;
		double angle = Double.valueOf((String) data.get("angle"));
		// get Player
		Player shooter = gameServer.getGameHandler().getPlayer(client);
		if (shooter == null) return null;
		
		int damage = 0;
		Player hittedPlayer = gameServer.getGameHandler().getHittedPlayer(shooter, angle);
		if (hittedPlayer != null) {
			// 부위별 데미지 구현은 추후에 할 예정
			damage = 30;
		}
		hittedPlayer.addHealth(-damage);
		hittedPlayer.sendHittedMsg(damage);
		if (hittedPlayer.getHealth()<=0) {
			hittedPlayer.sendDieMsg("killed-by-gun", shooter.getAccountId());
		}
		
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		
		resObj.put("action", "player_shoot");
		resData.put("damage", String.valueOf(damage));
		resObj.put("data", resData);
		return resObj.toJSONString();
	}

}
