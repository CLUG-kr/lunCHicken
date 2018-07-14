package com.clug.lunchicken;

import java.util.logging.Logger;

import com.clug.lunchicken.login.LoginServer;

public class LunChickenLoginServer {

	private static Logger logger = Logger.getLogger("LoginServer");
	
	/**
	 * Lunch + Chicken = LunChicken
	 * 런치킨 프로젝트는 현실에서 배틀로얄을 즐길 수 있게 해주는 프로젝트이다.
	 * 
	 * 이 파트는 서버 파트로써 런치킨 프로젝트에서 대부분의 데이터 처리를 맡고 있다.
	 * 런치킨 프로젝트의 서버는 로그인 서버와 게임 서버로 나뉘어 기능을 수행한다.
	 * 
	 */
	
	/**
	 * 1. 로그인 서버
	 * 유저들이 처음 접하는 서버이며, 회원가입과 로그인 과정을 통해 서버에 접근하게 된다.
	 * 게임 서버의 안정적인 유지를 위해 로그인 서버에 인증받지 못한 사용자들은 게임을 즐길 수 없도록 될 예정이다.
	 */
	public static LoginServer loginServer;
	
	/**
	 * 2. 게임 서버
	 * 유저들이 게임을 위해 데이터를 주고 받는 서버이다.
	 * 게임 참가부터 게임 승리까지 모든 데이터를 주고 받는다.
	 * public static GameServer gameServer;
	*/
	
	public static void main(String args[]) {
		logger.info("Login Server Made By Jo Mingyu");
		loginServer = new LoginServer(17777);
		loginServer.initSever();
		loginServer.openServer();
	}

}
