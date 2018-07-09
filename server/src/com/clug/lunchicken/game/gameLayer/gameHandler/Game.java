package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.List;

import com.clug.lunchicken.game.gameLayer.CircleZone;
import com.clug.lunchicken.game.gameLayer.Player;
import com.clug.lunchicken.game.gameLayer.Zone;

public class Game {

	private GameStatus gameStatus;
	/** 방에 들어와 있는 모든 인원들 */
	private List<Player> allPlayers;
	/** 죽거나 해서 게임을 플레이 할 수는 없지만 관전 중인 플레이어를 의미 */
	private List<Player> viewers;
	/** 게임을 플레이 하는 사람. 로비에서 대기 중인 사람도 포함된다  */
	private List<Player> livingPlayers;
	/** 방장 */
	private Player hostPlayer;
	
	// before game start
	private int gameId;
	private String gameName;
	private int currentPlayer;
	private int maxPlayer;
	
	// playing game
	/** 전체 맵의 범위. 모든 시간은 밀리 초 단위다 */
	private Zone playingZong;
	/** 자기장을 사용할 것인지 여부를 의미*/
	private boolean useSafeZone;
	/** 자기장이 몇 밀리초마다 바뀔 것인지 의미*/
	private int safeZoneReduceTime;
	/** 자기장이 줄어들기 남은 시간 */
	private int safeZoneTime;
	/** 자기장이 몇번 줄어들 것인지 */
	private int safeZoneReduceLevel;
	/** 지금은 몇번째 자기장인지 */
	private int safeZoneCurrentLevel;
	/** 현재 안전 구역 */
	private CircleZone safeZone;
	/** 다음 안전 구역 */
	private CircleZone nextSafeZone;
	
	/**
	 * 게임을 처음 만들 때 필요한 정보들을 가지고 게임 기본 데이터를 넣어주는 함수
	 * @param gameName
	 * @param maxPlayer
	 * @param playingZone
	 * @param useSafeZone
	 * @param safeZoneReduceTime
	 * @param safeZoneReduceLevel
	 */
	public void createGame(String gameName, int maxPlayer, Zone playingZone, boolean useSafeZone, int safeZoneReduceTime, int safeZoneReduceLevel) {
		this.setPlayingZong(playingZone);
		this.setGameName(gameName);
		this.setMaxPlayer(maxPlayer);
		this.setUseSafeZone(useSafeZone);
		this.setSafeZoneReduceTime(safeZoneReduceTime);
		this.setSafeZoneReduceLevel(safeZoneReduceLevel);
	}

	public GameStatus getGameStatus() {return gameStatus;}
	public void setGameStatus(GameStatus gameStatus) {this.gameStatus = gameStatus;}
	public int getGameId() {return gameId;}
	public void setGameId(int gameId) {this.gameId = gameId;}
	public Player getHostPlayer() {return hostPlayer;}
	public void setHostPlayer(Player hostPlayer) {this.hostPlayer = hostPlayer;}	
	
	public String getGameName() {return gameName;}
	public void setGameName(String gameName) {this.gameName = gameName;}
	public List<Player> getAllPlayers() {return allPlayers;}
	public void setAllPlayers(List<Player> allPlayers) {this.allPlayers = allPlayers;}
	public void addAllPlayer(Player player) {this.allPlayers.add(player);}
	public List<Player> getViewers() {return viewers;}
	public void setViewers(List<Player> viewers) {this.viewers = viewers;}
	public void addViewer(Player player) {this.viewers.add(player);}
	public List<Player> getLivingPlayers() {return livingPlayers;}
	public void setLivingPlayers(List<Player> livingPlayers) {this.livingPlayers = livingPlayers;}
	public void addLivingPlayer(Player player) {this.livingPlayers.add(player);}
	public int getMaxPlayer() {return maxPlayer;}
	public void setMaxPlayer(int maxPlayer) {this.maxPlayer = maxPlayer;}
	public int getCurrentPlayer() {return currentPlayer;}
	public void setCurrentPlayer(int currentPlayer) {this.currentPlayer = currentPlayer;}

	public Zone getPlayingZong() {return playingZong;}
	public void setPlayingZong(Zone playingZong) {this.playingZong = playingZong;}
	public boolean isUseSafeZone() {return useSafeZone;}
	public void setUseSafeZone(boolean useSafeZone) {this.useSafeZone = useSafeZone;}
	public int getSafeZoneReduceTime() {return safeZoneReduceTime;}
	public void setSafeZoneReduceTime(int safeZoneReduceTime) {this.safeZoneReduceTime = safeZoneReduceTime;}
	public int getSafeZoneTime() {return safeZoneTime;}
	public void setSafeZoneTime(int safeZoneTime) {this.safeZoneTime = safeZoneTime;}
	public void addSafeZoneTime(int safeZoneTime) {this.safeZoneTime += safeZoneTime;}
	public int getSafeZoneReduceLevel() {return safeZoneReduceLevel;}
	public void setSafeZoneReduceLevel(int safeZoneReduceLevel) {this.safeZoneReduceLevel = safeZoneReduceLevel;}
	public int getSafeZoneCurrentLevel() {return safeZoneCurrentLevel;}
	public void setSafeZoneCurrentLevel(int safeZoneCurrentLevel) {this.safeZoneCurrentLevel = safeZoneCurrentLevel;}
	public CircleZone getSafeZone() {return safeZone;}
	public void setSafeZone(CircleZone safeZone) {this.safeZone = safeZone;}
	public CircleZone getNextSafeZone() {return nextSafeZone;}
	public void setNextSafeZone(CircleZone nextSafeZone) {this.nextSafeZone = nextSafeZone;}

}
