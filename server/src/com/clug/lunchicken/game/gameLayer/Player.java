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
	
}