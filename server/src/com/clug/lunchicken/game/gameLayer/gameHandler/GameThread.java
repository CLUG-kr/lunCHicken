package com.clug.lunchicken.game.gameLayer.gameHandler;

import com.clug.lunchicken.game.gameLayer.CircleZone;
import com.clug.lunchicken.game.gameLayer.Location;
import com.clug.lunchicken.game.gameLayer.Player;

/**
 * 게임을 관리할 쓰레드. 게임 스타트가 되면 생성된다.
 * @author owlsogul
 */
public class GameThread implements Runnable{

	private Thread gameThread;
	private Game game;
	private GameHandler gameHandler;
	
	private long lastUpdate = 0;
	
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
		// 첫번째 자기장을 계산함
		if (game.isUseSafeZone()) {
			calculateNextSafeZone();
		}
		
		// 게임의 상태를 플레잉 상태로 바꿈
		game.setGameStatus(GameStatus.GAME_PLAYING);
		
		// 게임이 끝나고 Dead 상태가 되기 전까지 반복
		while (game.getGameStatus() != GameStatus.GAME_DEAD) {
			
			// 해야 할 일 들
			// 1. 자기장 시간
			// 2. 자기장을 움직임 -> 지금 단계에서는 바로 줄어드는거로 바꾸자
			// 3. 자기장에 데이는 사람들에게 알림
			if (game.isUseSafeZone()) {
				// 1. 자기장 시간 조절
				// 최근 업데이트 시간과 지금 시간을 비교해서 그 갭만큼 빼줌
				long currentTime = System.currentTimeMillis();
				int timeGap = (int)(currentTime - lastUpdate); 
				if (timeGap == 0) { // 차이가 안날 경우,,,
					timeGap = 1;
				}
				game.addSafeZoneTime(-timeGap);
				lastUpdate = System.currentTimeMillis();
				
				// 2. 자기장을 움직임
				if (game.getSafeZoneTime() <= 0) {
					// 현재 자기장을 다음 자기장으로 옮기고
					game.setSafeZone(game.getNextSafeZone());
					// 다음 자기장 계산
					calculateNextSafeZone();
				}
				
				// 3. 자기장에 데이는 사람들에게 알림
				Location centerOfSafeZone = game.getSafeZone().getCenter();
				double radiusOfSafeZone = game.getSafeZone().getRadius();
				currentTime = System.currentTimeMillis();
				for (Player player : game.getLivingPlayers()) {
					if (player.getLocation().getDistance(centerOfSafeZone) > radiusOfSafeZone) {
						// TODO: notice
						if (currentTime - player.getHealthLastUpdate() >= 1000) {
							int damageOfSafeZone = game.getSafeZoneCurrentLevel();
							player.addHealth(-damageOfSafeZone);
							player.setHealthLastUpdate(currentTime);
						}
					}
				}
			}
			
			// 4. 플레이어들 체력 확인해서 죽었는지 결정
			// 5. 게임 끝날 지 말지 결정
			// * 총 맞았는지 판별은 클라이언트에서 데이터가 와야지 진행하는 것이기 떄문에 진행하지 않음
			
		}
		
		// Dead 상태가 됬으면
		destroyGame();
	}
	
	private void calculateNextSafeZone() {
		// 안전지역을 구하는 메소드
		// 첫번째 안전지역일 경우 playingZone에서 만들어질 수 있는 가장 큰 원의 70퍼센트의 크기의 원을 만든다
		// 아닐 경우 안전지역 레벨이 맞는지 확인하고 자기장을 구한다. 이전 자기장 크기의 55퍼센트이다.
		if (game.getSafeZoneCurrentLevel() == 0) { //첫 자기장일 경우
			
			double width = Math.abs(game.getPlayingZong().getLocation2().getPosX() - game.getPlayingZong().getLocation1().getPosX());
			double height = Math.abs(game.getPlayingZong().getLocation2().getPosY() - game.getPlayingZong().getLocation1().getPosY());
			double radius = ((width < height ? width : height) / 2) * 0.7;
			
			double minX = game.getPlayingZong().getLocation1().getPosX() + radius;
			double maxX = game.getPlayingZong().getLocation2().getPosX() - radius;
			double x = minX + (Math.random() * (maxX - minX));
					
			double minY = game.getPlayingZong().getLocation2().getPosY() + radius;
			double maxY = game.getPlayingZong().getLocation1().getPosY() - radius;
			double y = minY + (Math.random() * (maxY - minY));
			Location center = new Location(x, y);
			game.setNextSafeZone(new CircleZone(center, radius));
			
		}
		else { // 첫 자기장이 아닐 경우
			
			double radius = game.getSafeZone().getRadius() * 0.55;
			
			// 센터의 위치
			// 원의 내부여야하며
			// 센터로 부터 만들어지는 원이 원을 넘어가면 안됨
			
			// 방식 1. 조건 성립할 때 까지 랜덤을 돌린다.
			// 방식 2. x 좌표 결정 후 y 의 최소값 및 최대값 계산 -> 랜덤으로 y좌표 결정
			// 방식 2를 사용한다.
			double originR = game.getSafeZone().getRadius();
			double originCenterY = game.getSafeZone().getCenter().getPosY(); 
			double originCenterX = game.getSafeZone().getCenter().getPosX();
			
			double minX = originCenterX - originR + radius;
			double maxX = originCenterX + originR - radius;
			double x = minX + (Math.random() * (maxX - minX));
			
			double temp1 = (originR - radius - x + originCenterX)*(originR - radius + x - originCenterX);
			double temp2 = Math.sqrt(temp1);
			double minY = originCenterY - temp2;
			double maxY = originCenterY + temp2;
			double y = minY +  Math.random()*(maxY - minY);
			
			game.setNextSafeZone(new CircleZone(new Location(x, y), radius));
		}
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
