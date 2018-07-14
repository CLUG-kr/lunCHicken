package com.clug.lunchicken.game.gameLayer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.gameLayer.gameHandler.Game;

public class Player {

	private Client client;
	// account
	private String accountId;
	
	// game room
	private int joinedGameId;
	private Game joinedGame;
	
	// game
	private int health;
	private long healthLastUpdate = 0;
	private Location location;
	
	public Player(Client client, String accountId) {
		this.setClient(client);
		this.setAccountId(accountId);
	}
	
	public Client getClient() {return client;}
	public void setClient(Client client) {this.client = client;}
	public String getAccountId() {return accountId;}
	public void setAccountId(String accountId) {this.accountId = accountId;}
	
	public void leaveGame() {
		joinedGameId = -1;
		joinedGame = null;
	}
	public void joinGame(Game game) {
		setJoinedGame(game);
		setJoinedGameId(game.getGameId());
	}
	
	public int getJoinedGameId() {return joinedGameId;}
	public void setJoinedGameId(int joinedGameId) {this.joinedGameId = joinedGameId;}
	public Game getJoinedGame() {return joinedGame;}
	public void setJoinedGame(Game joinedGame) {this.joinedGame = joinedGame;}
	
	public int getHealth() {return health;}
	public void setHealth(int health) {this.health = health;}
	public void addHealth(int health) {this.health += health;}
	
	public long getHealthLastUpdate() {return healthLastUpdate;}
	public void setHealthLastUpdate(long healthLastUpdate) {this.healthLastUpdate = healthLastUpdate;}
	
	public Location getLocation() {return location;}
	public void setLocation(Location location) {this.location = location;}

	/**
	 * 레디 룸 상태의 게임의 정보를 전송하는 메소드. 메세지 형식은 다음과 같다.<br>
	 * Message: {
	 * 	"action":"ready_room_info",
	 * 	"data":{
	 * 		"game_id":"(gameId)",
	 * 		"game_name":"(gameName)",
	 * 		"current_player":"(currentPlayer)",
	 * 		"max_player":"(maxPlayer)",
	 * 		"players":{
	 * 			{
	 * 				"accountId":"(accountId)",
	 * 				"isHost":"(true or false)"
	 * 			},
	 * 			{
	 * 				"accountId":"(accountId)",
	 * 				"isHost":"(true or false)"
	 * 			}, 
	 * 			...
	 * 		}
	 * 	}
	 * }
	 */
	@SuppressWarnings("unchecked")
	public void sendReadyRoomMsg() {
		
		// 게임에 들어갔는지 여부 확인
		// 멀티쓰레딩 과정에서 널 포인터 오류가 날 수 있기 때문에
		if (this.getJoinedGame() == null) return;
		Game game = this.getJoinedGame();
		
		// JSON parsing
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "ready_room_info");
		resData.put("game_id", game.getGameId());
		resData.put("game_name", game.getGameName());
		resData.put("current_player", String.valueOf(game.getCurrentPlayer()));
		resData.put("max_player", String.valueOf(game.getMaxPlayer()));
		JSONArray playerArr = new JSONArray();
		for (Player p : game.getAllPlayers()) {
			JSONObject playerObj = new JSONObject();
			playerObj.put("accountId", p.getAccountId());
			playerObj.put("isHost", String.valueOf(game.getHostPlayer() == p));
			playerArr.add(playerObj);
		}
		resData.put("players", playerArr);
		resObj.put("data", resData);
		
		// send
		client.send(resObj.toJSONString());
	}
	
	/**
	 * 게임이 시작되었을 때 메세지를 전송하는 메소드<br>
	 * Message: {
	 * 	"action":"game_start",
	 * 	"data":{(sendReadyRoomMsg 와 같다.)}
	 * }
	 * @see sendReadyRoomMsg
	 */
	@SuppressWarnings("unchecked")
	public void sendGameStartMsg() {
		// 게임에 들어갔는지 여부 확인
		// 멀티쓰레딩 과정에서 널 포인터 오류가 날 수 있기 때문에
		if (this.getJoinedGame() == null) return;
		Game game = this.getJoinedGame();
		
		// JSON parsing
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "game_start");
		resData.put("game_id", game.getGameId());
		resData.put("game_name", game.getGameName());
		resData.put("current_player", String.valueOf(game.getCurrentPlayer()));
		resData.put("max_player", String.valueOf(game.getMaxPlayer()));
		JSONArray playerArr = new JSONArray();
		for (Player p : game.getAllPlayers()) {
			JSONObject playerObj = new JSONObject();
			playerObj.put("accountId", p.getAccountId());
			playerObj.put("isHost", String.valueOf(game.getHostPlayer() == p));
			playerArr.add(playerObj);
		}
		resData.put("players", playerArr);
		resObj.put("data", resData);
		
		// send
		client.send(resObj.toJSONString());
	}
	
	/**
	 * 안전 구역이 줄어들었을 때 메세지를 보내는 메소드<br>
	 * Message : {
	 * 	"action":"game_safe_zone_reduce",
	 * 	"data":{
	 * 		"safe_zone":{
	 * 			"center":{
	 * 				"x":"",
	 * 				"y":"",
	 * 			},
	 * 			"radius":""
	 * 		}
	 * 	}
	 * }
	 */
	@SuppressWarnings("unchecked")
	public void sendSafeZoneReduceMsg() {
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "game_safe_zone_reduce");
		JSONObject safeZoneObj = new JSONObject();
		JSONObject centerObj = new JSONObject();
		CircleZone safeZone = this.getJoinedGame().getSafeZone();
		Location center = safeZone.getCenter();
		centerObj.put("x", String.valueOf(center.getPosX()));
		centerObj.put("y", String.valueOf(center.getPosY()));
		safeZoneObj.put("cetner", centerObj);
		safeZoneObj.put("radius", safeZone.getRadius());
		resData.put("safe_zone", safeZoneObj);
		resObj.put("data", resData);
		client.send(resObj.toJSONString());
	}
	
	/**
	 * SafeZone 에 데미지 입을 경우 메세지를 보내는 메소드<br>
	 * Message :{
	 * 	"action":"game_safe_zone_damage",
	 * 	"data":{
	 * 		"damage":"",
	 * 		"health":""
	 * 	}
	 * }
	 */
	@SuppressWarnings("unchecked")
	public void sendSafeZoneDamageMsg(int damage) {
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "game_safe_zone_damage");
		resData.put("damage", String.valueOf(damage));
		resData.put("health", String.valueOf(this.getHealth()));
		resObj.put("data", resData);
		client.send(resObj.toJSONString());
	}
	
	/**
	 * 죽었을 경우 메세지를 보내는 메소드<br>
	 * Message :{
	 * 	"action":"game_die",
	 * 	"data":{
	 * 		"reason:"",
	 * 		"killer":""
	 * 	}
	 * }
	 */
	@SuppressWarnings("unchecked")
	public void sendDieMsg(String reason, String killer) {
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "game_die");
		resData.put("reason", reason);
		resData.put("killer", killer);
		resObj.put("data", resData);
		client.send(resObj.toJSONString());
	}
	
	public void sendHittedMsg() {
		
	}
	
}