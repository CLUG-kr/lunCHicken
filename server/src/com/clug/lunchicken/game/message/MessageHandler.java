package com.clug.lunchicken.game.message;

import java.util.HashMap;

import com.clug.lunchicken.game.GameServer;


public class MessageHandler {
	
	public final static String NO_RESPONSE = "no-response";
	
	private HashMap<String, Message> messageMap;
	private GameServer gameServer;
	
	public MessageHandler(GameServer gameServer) {
		this.gameServer = gameServer;
		messageMap = new HashMap<>();
		initMessageHandler();
	}
	
	private void initMessageHandler() {
		/*
		 * 1. MsgRegisterPlayer
		 * - 가장 처음 플레이어를 등록
		 * - 성공 했는지 실패했는지 보냄
		 * 2. MsgShowGameList
		 * - 방 리스트를 보여줌
		 * 3. MsgJoinGame
		 * - 방에 들어감
		 * - 방에 있는 다른 플레이어들에게 방 정보 전송
		 * 4. MsgCreateGame
		 * - 방을 만듦
		 * 5. MsgLeaveGame
		 * - 방에서 나감
		 * - 방에 있는 다른 플레이어에게 방 정보 전송
		 * 6. MsgStartGame
		 * - 게임을 시작함
		 * - 모든 플레이어에게 전달
		 * 7. MsgReadyRoomInfo
		 * - 방 정보 전달 메세지 
		 * - 서버에서 클라이언트로만
		 * - Player 의 sendReadyRoomInfo 메소드로!
		 * 
		 * 1. MsgShoot
		 * - 총을 쐈을 때
		 * - 명중했는지 응답. 맞은 사람한테는 MsgHealthChange 보내기
		 * 2. MsgPosition
		 * - 클라이언트가 주기적으로 보내는 위치 데이터
		 * - 응답 없음
		 * 3. MsgHealthChange
		 * - 자기장을 맞았거나 총을 맞았을 때
		 * 4. MsgSafeZone
		 * - 자기장에 대한 정보
		 */
		messageMap.put("register_player", new MsgRegisterPlayer(gameServer));
		messageMap.put("show_game_list", new MsgShowGameList(gameServer));
		messageMap.put("join_game", new MsgJoinGame(gameServer));
		messageMap.put("create_game", new MsgCreateGame(gameServer));
		messageMap.put("leave_game", new MsgLeaveGame(gameServer));
		messageMap.put("start_game", new MsgStartGame(gameServer));
		messageMap.put("player_shoot", new MsgShoot(gameServer));
	}
	
	public Message getMessageHandler(String action) {
		if (messageMap.containsKey(action)) {
			return messageMap.get(action);
		}
		return null;
	}
}
