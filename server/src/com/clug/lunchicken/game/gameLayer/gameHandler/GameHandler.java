package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.clug.lunchicken.game.Client;
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
	private HashMap<Client, Player> players;
	
	public GameHandler(GameServer gameServer) {
		gameList = new LinkedList<>();
		gameThreadList = new LinkedList<>();
		players = new HashMap<>();
		this.gameServer = gameServer;
	}
	
	/**
	 * 클라이언트 데이터를 통해 플레이어를 등록하는 메소드
	 * 중복방지가 들어가 있다.
	 * @param client
	 * @param accountId
	 */
	@Override
	public void registerPlayer(Client client, String accountId) {
		if (players.containsKey(client)) {
			return;
		}
		Player player = new Player(client, accountId);
		players.put(client, player);
	}
	
	/**
	 * 클라이언트에 대응하는 플레이어를 가져오는 메소드
	 * @param client 플레이어에 대응하는 클라이언트. 존재하지 않다면 null 을 반환한다.
	 */
	@Override
	public Player getPlayer(Client client) {
		return players.get(client);
	}
	
	/**
	 * 클라이언트에 대응하는 플레이어를 제거한다.
	 * @param client
	 */
	@Override
	public void unregisterPlayer(Client client) {
		players.remove(client);
	}
	
	/**
	 * 플레이어를 제거한다.
	 * @param player
	 */
	@Override
	public void unregisterPlayer(Player player) {
		unregisterPlayer(player.getClient());
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

	/**
	 * shooter가 총을 쐈을 때 맞은 사람을 찾는 메소드
	 * @param shooter 총을 쏜 플레이어
	 * @param degree 복쪽 방향으로 부터의 각도. 
	 */
	private static final double ErrorFactor = 0.1;
	@Override
	public Player getHittedPlayer(Player shooter, double degree) {
		// 좌표와 각도를 통해 맞았는지 안맞았는지를 확인
		
		// 방법 1. 일차 함수를 만들어서 해당 일차 함수에 포함 되는지 확인
		// 단점: 사람의 몸은 점이 아니기도 하고, 오차율도 포함 해야하므로 
		
		// 방법2. 기울기만을 비교하여 처리
		// 단점: 방법1 보다는 연산이 적지만 여전히 사람을 점 취급
		
		// 테스트 단계에서는 일단 방법2를 사용한다.
		// 일단 맞은 사람을 모두 구한다.
		Game game = shooter.getJoinedGame();
		class ProcessUnit {
			Player player;
			double distance;
			ProcessUnit(Player player, double distance){
				this.player = player;
				this.distance = distance;
			}
		}
		Vector<ProcessUnit> hittedPlayers = new Vector<>();
		double gradient = Math.pow(Math.tan(Math.toRadians(degree)), -1);
		for (Player p : game.getLivingPlayers()) {
			double newX = p.getLocation().getPosX() - shooter.getLocation().getPosX();
			double newY = p.getLocation().getPosY() - shooter.getLocation().getPosY();
			double degreeBetweenPlayerAndShooter = newY/newX;
			if ((degree <= 180 & shooter.getLocation().getPosX() <= p.getLocation().getPosX() && shooter.getLocation().getPosY() <= p.getLocation().getPosY()) ||
				(degree >= 180 & shooter.getLocation().getPosX() >= p.getLocation().getPosX() && shooter.getLocation().getPosY() >= p.getLocation().getPosY())	) {
				if (gradient*(1-ErrorFactor) <= degreeBetweenPlayerAndShooter && degreeBetweenPlayerAndShooter <= gradient*(1+ErrorFactor)) {
					hittedPlayers.add(new ProcessUnit(p, p.getLocation().getDistance(shooter.getLocation())));
				}
			}
		}
		
		// 가장 가까운 사람을 맞힌다.
		if (hittedPlayers.size() > 0) {
			ProcessUnit closestPlayer = hittedPlayers.firstElement();
			for (ProcessUnit pu : hittedPlayers) {
				if (closestPlayer.distance > pu.distance) {
					closestPlayer = pu;
				}
			}
			return closestPlayer.player;
		}
		else {
			return null;	
		}
	}
	
	
	
}
