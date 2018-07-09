package com.clug.lunchicken.game.gameLayer;

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
	
	public void leaveGame() {
		joinedGameId = -1;
		joinedGame = null;
	}
	public void joinGame(Game game) {
		setJoinedGame(game);
		setJoinedGameId(game.getGameId());
	}
	public int getJoinedGameId() {
		return joinedGameId;
	}
	public void setJoinedGameId(int joinedGameId) {
		this.joinedGameId = joinedGameId;
	}
	public Game getJoinedGame() {
		return joinedGame;
	}
	public void setJoinedGame(Game joinedGame) {
		this.joinedGame = joinedGame;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public void addHealth(int health) {
		this.health += health;
	}	
	public long getHealthLastUpdate() {
		return healthLastUpdate;
	}
	public void setHealthLastUpdate(long healthLastUpdate) {
		this.healthLastUpdate = healthLastUpdate;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

	
}