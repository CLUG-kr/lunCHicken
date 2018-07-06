package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.List;

import com.clug.lunchicken.game.gameLayer.Player;

public interface IGameHandler {

	// in lobby
	public int joinGame(Player player, int gameId);
	public void createGame(Player player, Game rawGame);
	public List<Game> getGameList();
	
	// in ready room
	public void leaveGame(); // 게임 중일 때 동일
	public void startGame(); // 게임 시작
	
	// in Game
	/*public void playerMove();
	public void playerShoot();
	public void playerHitted();
	public void playerDie();
	public void playerKill();*/
}
