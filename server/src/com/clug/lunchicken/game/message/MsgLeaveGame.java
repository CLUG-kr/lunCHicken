package com.clug.lunchicken.game.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.game.Client;
import com.clug.lunchicken.game.GameServer;
import com.clug.lunchicken.game.gameLayer.Player;
import com.clug.lunchicken.game.gameLayer.gameHandler.Game;
import com.clug.lunchicken.game.gameLayer.gameHandler.GameStatus;
/**
 * request:
 * {
 * 	"action":"leave_game",
 * 	"data":{}
 * }<br>
 * response: no-response
 * 
 * @author JoMingyu
 *
 */
public class MsgLeaveGame extends Message {

	public MsgLeaveGame(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public String handleMessage(Client client, JSONObject data) {
		
		Player player = gameServer.getGameHandler().getPlayer(client);
		if (player == null) return null;
		if (player.getJoinedGame() == null) return null;

		Game game = player.getJoinedGame();
		gameServer.getGameHandler().leaveGame(player);
		
		// 게임이 대기실이라면 모든 사람들에게 대기실 변경 사항을 보냄
		if (game.getGameStatus() == GameStatus.GAME_READYROOM) {
			for (Player p : game.getAllPlayers()) {
				p.sendReadyRoomMsg();
			}	
		}
		
		return MessageHandler.NO_RESPONSE;
	}

}
