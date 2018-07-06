package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.List;

import com.clug.lunchicken.game.gameLayer.Player;
import com.clug.lunchicken.game.gameLayer.Zone;

public class Game {

	private GameStatus gameStatus;
	/**
	 * 방에 들어와 있는 모든 인원들
	 */
	private List<Player> allPlayers;
	/**
	 * 죽거나 해서 게임을 플레이 할 수는 없지만 관전 중인 플레이어를 의미
	 */
	private List<Player> viewers;
	/**
	 * 게임을 플레이 하는 사람.
	 * 로비에서 대기 중인 사람도 포함된다
	 */
	private List<Player> livingPlayers;
	private Player hostPlayer;
	
	// before game start
	private int gameId;
	private String gameName;
	private int currentPlayer;
	private int maxPlayer;
	
	// playing game
	private boolean useSafeZone;
	private int safeZoneReduceTime;
	private Zone safeZone;
	private Zone nextSafeZone;
	
	public void createGame(String gameName, int maxPlayer, boolean useSafeZone, int safeZoneReduceTime) {
		this.gameName = gameName;
		this.setMaxPlayer(maxPlayer);
		this.useSafeZone = useSafeZone;
		this.safeZoneReduceTime = safeZoneReduceTime;
	}

	public GameStatus getGameStatus() {return gameStatus;}
	public void setGameStatus(GameStatus gameStatus) {this.gameStatus = gameStatus;}
	public int getGameId() {return gameId;}
	public void setGameId(int gameId) {this.gameId = gameId;}
	public Player getHostPlayer() {return hostPlayer;}
	public void setHostPlayer(Player hostPlayer) {this.hostPlayer = hostPlayer;}

	public List<Player> getAllPlayers() {return allPlayers;}
	public void setAllPlayers(List<Player> allPlayers) {this.allPlayers = allPlayers;}
	public void addAllPlayer(Player player) {this.allPlayers.add(player);}

	public List<Player> getViewers() {return viewers;}
	public void setViewers(List<Player> viewers) {this.viewers = viewers;}
	public void addViewer(Player player) {this.viewers.add(player);}

	public List<Player> getLivingPlayers() {return livingPlayers;}
	public void setLivingPlayers(List<Player> livingPlayers) {this.livingPlayers = livingPlayers;}
	public void addLivingPlayer(Player player) {
		this.livingPlayers.add(player);
		this.currentPlayer += 1;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
}
