package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.List;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.gameLayer.Player;

public interface IGameHandler {

	public void registerPlayer(Client client, String accountId);
	public Player getPlayer(Client client);
	public void unregisterPlayer(Client client);
	public void unregisterPlayer(Player player);
	
	// in lobby
	public int joinGame(Player player, int gameId);
	public void createGame(Player player, Game rawGame);
	public List<Game> getGameList();
	
	// in ready room
	public void leaveGame(Player player); // 게임 중일 때 동일
	public void startGame(Player player); // 게임 시작
	
	// in Game
	/*public void playerMove();
	public void playerShoot();
	public void playerHitted();
	public void playerDie();
	public void playerKill();*/
}
