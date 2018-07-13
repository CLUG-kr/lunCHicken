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
		resData.put("players", playerArr.toJSONString());
		resObj.put("data", resData.toJSONString());
		
		// send
		client.send(resObj.toJSONString());
	}
	
	public void sendGameStartMsg() {
		
	}
	
	public void sendSafeZoneReduceMsg() {
		
	}
	
	public void sendSafeZoneDamageMsg() {
		
	}
	
	public void sendDieMsg() {
		
	}
	
	public void sendHittedMsg() {
		
	}
	
}