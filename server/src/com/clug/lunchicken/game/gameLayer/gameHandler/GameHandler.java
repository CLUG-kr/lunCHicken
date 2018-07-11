package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.LinkedList;
import java.util.List;

import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;

public class GameHandler implements IGameHandler{
	
	public static int JOIN_SUCCESS = 0;
	public static int JOIN_FAIL_ALREADY_JOIN = 100;
	public static int JOIN_FAIL_FULL = 101;
	public static int JOIN_FAIL_NOT_READY = 102;
	
	private GameServer gameServer;
	private List<Game> gameList;
	private List<GameThread> gameThreadList;
	public GameHandler(GameServer gameServer) {
		gameList = new LinkedList<>();
		gameThreadList = new LinkedList<>();
		this.gameServer = gameServer;
	}
	
	/**
	 * 게임에 가입하는 메소드
	 * 클라이언트 측에서는 방에 접근 하는 방법이 gameId 밖에 없다.
	 * @param 
	 * Player player - 가입하고자 하는 사람
	 * int gameId - 가입하고자 하는 방의 아이디
	 * @return
	 * 성공 : 0<br>
	 * 실패 : >0<br>
	 * - 플레이어가 이미 다른 방에 들어가 있을 경우<br>
	 * - 이미 방이 꽉차 있는 경우<br>
	 * - 들어가고자 하는 방이 레디 상태가 아닐 경우<br>
	 */
	@Override
	public int joinGame(Player player, int gameId) {
		
		// 이미 플레이어가 다른 방에 존재하고 있을 경우
		if (player.getJoinedGame() != null && player.getJoinedGameId() != -1) {
			return JOIN_FAIL_ALREADY_JOIN;
		}
		
		// 들어가고자 하는 방이 레디가 아닐 경우
		Game game = findGameById(gameId);
		if (game == null || game.getGameStatus() != GameStatus.GAME_READYROOM) {
			return JOIN_FAIL_NOT_READY;
		}
		
		// 이미 방이 꽉차 있는 경우
		if (game.getCurrentPlayer() >= game.getMaxPlayer()) {
			return JOIN_FAIL_FULL;
		}
		
		game.addAllPlayer(player);
		game.addLivingPlayer(player);
		game.setCurrentPlayer(game.getCurrentPlayer()+1);
		player.setJoinedGame(game);
		player.setJoinedGameId(game.getGameId());
		return JOIN_SUCCESS;
	}
	
	/**
	 * 게임 아이디로 게임을 찾아주는 메소드
	 * @param gameId
	 * @return 없을 경우 null
	 */
	private Game findGameById(int gameId) {
		for (Game game : gameList) {
			if (game.getGameId() == gameId) {
				return game;
			}
		}
		return null;
	}
	
	/**
	 * 게임을 만드는 메소드.
	 * @param
	 * Player player - 게임을 만든 사람
	 * Game rawGame - Game 객체의 createGame 메소드를 수행한 게임 객체. 대기실에서 필수적인 정보만 존재한다.
	 */
	@Override
	public void createGame(Player player, Game rawGame) {
		
		// 게임의 스테이터스를 바꾸고
		rawGame.setGameStatus(GameStatus.GAME_READYROOM);
		// 아이디 설정
		rawGame.setGameId(makeUniqueGameId());
		// 호스트 설정
		rawGame.setHostPlayer(player);
		// 플레이어 리스트에 기입
		rawGame.addAllPlayer(player);
		rawGame.addLivingPlayer(player);
		
		// 플레이어 방 가입
		player.joinGame(rawGame);
		
	}
	
	/**
	 * 방의 아이디를 만드는 메소드. 겹쳐지는 것이 없게 synchronized 되어 있다.
	 * @return 아이디
	 */
	private synchronized int makeUniqueGameId() {
		int key;
		boolean isDuplicated = false;
		do {
			key = (int) (Math.random()*1000000);
			for (Game game : gameList) {
				if (game.getGameId() == key) {
					isDuplicated = true;
					break;
				}
			}
		} while(isDuplicated);
		return key;
	}

	/**
	 * 지금 만들어져 있는 게임 리스트를 가지고 온다.
	 * 추후에 필터 기능도 추가할 예정
	 * @return 현재 모든 게임 리스트
	 */
	@Override
	public List<Game> getGameList() {
		return gameList;
	}

	
	/**
	 * 플레이어를 현재 포함되어 있는 게임에서 내보낸다.
	 * 만약 나간 플레이어가 host 였으면 호스트가 없어진다.
	 * @param player 게임에서 나갈 플레이어
	 */
	@Override
	public void leaveGame(Player player) {
		
		// Game 에서 플레이어를 지워야 함
		Game game = player.getJoinedGame();
		if (game == null) return;
		game.getAllPlayers().remove(player);
		game.getLivingPlayers().remove(player);
		game.getViewers().remove(player);
		if (game.getHostPlayer().equals(player)) game.setHostPlayer(null);
		game.setCurrentPlayer(game.getCurrentPlayer()-1);
		
		// Player 에서 Game 을 지워야함
		player.setJoinedGame(null);
		player.setJoinedGameId(-1);
		
	}

	/**
	 * 게임을 시작하게 한다. 게임 스테이터스가 스타트로 변경
	 * @param player 게임을 시작하자고 하는 플레이어
	 */
	@Override
	public void startGame(Player player) {
		Game game = player.getJoinedGame();
		game.setGameStatus(GameStatus.GAME_START);
		GameThread gameThread = new GameThread(this, game);
		gameThreadList.add(gameThread);
		gameThread.startThread();
	}
	
	public List<GameThread> getGameThreadList(){
		return this.gameThreadList;
	}
	
	
	
}
