package com.clug.lunchicken.login.account;

import com.clug.lunchicken.login.Client;

public interface IAccountManager {
	/**
	 * 토큰 - 클라이언트의 맵이 필요하다.
	 * 암호키를 저장하고 있어야 합니다.
	 */
	
	/**
	 * MessageLogin 에서 로그인에 성공했을 경우 실행되는 메소드
	 * AccountManager에서 처음으로 실행될 메소드
	 * 이미 클라이언트가 연결되어 있을 경우 두 개다 연결을 끊음.
	 * 
	 * 1. 토큰이 없을 경우 -> 토큰 생성 후 등록
	 * 2. 토큰이 있다면 -> 토큰 유효성 확인 -> 클라이언트 연결관계 파악 후 이미 연결되어 있다면 둘다 끊고, 아니면 변경
	 * 3. 토큰을 리턴함.
	 */
	public String registerAccount(String id, String token, Client client);
	
	/**
	 * 토큰의 유효성을 확인함
	 */
	public boolean validateToken(String id, String token);
	
	/**
	 *	현재 시점에서 30분 만료시간을 가진 토큰을 만든다. 
	 */
	public String makeToken(String id);
	
	public void removeToken(String key);
}
