package com.clug.lunchicken.game.gameLayer.gameHandler;

public enum GameStatus {
	
	/**
	 * 1단계
	 * 게임이 대기실에 있는 상태
	 */
	GAME_READYROOM(0),
	/**
	 * 2단계
	 * 게임이 시작되는 중
	 */
	GAME_START(1),
	/**
	 * 3단계
	 * 게임이 진행 중
	 */
	GAME_PLAYING(2),
	/**
	 * 4단계
	 * 게임이 끝남
	 */
	GAME_FINISH(3),
	/**
	 * 5단계
	 * 게임이 없어지는 중
	 */
	GAME_DEAD(4);
	
	private int level;
	GameStatus(int level) {
		this.level = level;
	}
	
	int getLevel() {
		return this.level;
	}
}
