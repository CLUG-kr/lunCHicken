package com.clug.lunchicken.game.gameLayer.gameHandler;

import java.util.LinkedList;
import java.util.List;

import com.clug.lunchicken.game.gameLayer.Player;

public class GameHandler implements IGameHandler{

	private List<Game> gameList;
	public GameHandler() {
		gameList = new LinkedList<>();
	}

	@Override
	public void joinGame() {
		// TODO Auto-generated method stub
		
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

	@Override
	public void showGameList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
