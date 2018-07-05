package com.clug.lunchicken.game.gameLayer.gameHandler;

public enum GameStatus {
	
	/**
	 * 1단계
	 * 게임이 대기실에 있는 상태
	 */
	GAME_READYROOM,
	/**
	 * 2단계
	 * 게임이 시작되는 중
	 */
	GAME_START,
	/**
	 * 3단계
	 * 게임이 진행 중
	 */
	GAME_PLAYING,
	/**
	 * 4단계
	 * 게임이 끝남
	 */
	GAME_FINISH,
	/**
	 * 5단계
	 * 게임이 없어지는 중
	 */
	GAME_DEAD,
	
}
