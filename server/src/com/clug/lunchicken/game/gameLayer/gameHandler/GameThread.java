package com.clug.lunchicken.game.gameLayer.gameHandler;

import com.clug.lunchicken.game.gameLayer.Player;

/**
 * 게임을 관리할 쓰레드. 게임 스타트가 되면 생성된다.
 * @author owlsogul
 */
public class GameThread implements Runnable{

	private Thread gameThread;
	private Game game;
	private GameHandler gameHandler;
	public GameThread(GameHandler gameHandler, Game game) {
		this.gameHandler = gameHandler;
		this.game = game;
		this.gameThread = new Thread(this);
	}
	
	public void startThread() {
		gameThread.start();
	}
	
	@Override
	public void run() {
		// 게임의 상태를 플레잉 상태로 바꿈
		game.setGameStatus(GameStatus.GAME_PLAYING);
		
		// 게임이 끝나고 Dead 상태가 되기 전까지 반복
		while (game.getGameStatus() != GameStatus.GAME_DEAD) {
			
			// 자기장을 움직임
			// 자기장에 데이는 사람들에게 알림
			// 게임 끝날 지 말지 결정
			// 총 맞았는지 판별은 클라이언트에서 데이터가 와야지 진행하는 것이기 떄문에 진행하지 않음
			
		}
		
		// Dead 상태가 됬으면
		destroyGame();
	}
	
	private void destroyGame() {
		
		// 게임 핸들러에서 게임 제거
		gameHandler.getGameList().remove(game);

		// 플레이어에게서 게임 제거
		for (Player player : game.getAllPlayers()) {
			player.leaveGame();
		}
		
		// 게임에서 플레이어 제거
		game.setAllPlayers(null);
		game.setLivingPlayers(null);
		game.setViewers(null);
		
		// 이 객체에서 게임 제거 및 이 객체 제거
		game = null;
		gameHandler.getGameThreadList().remove(this);
	}

}
