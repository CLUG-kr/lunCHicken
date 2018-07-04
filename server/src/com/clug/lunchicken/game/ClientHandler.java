package com.clug.lunchicken.game;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.clug.lunchicken.game.message.Message;
import com.clug.lunchicken.game.message.MessageHandler;

public class ClientHandler {
	private GameServer gameServer;
	private List<Client> clientList;
	private MessageHandler messageHandler;
	
	public ClientHandler(GameServer gameServer) {
		this.gameServer = gameServer;
		clientList = new LinkedList<>();
		messageHandler = new MessageHandler(gameServer);
	}
	
	public void registerClient(Socket socket) throws IOException {
		System.out.println(socket);
		Client client = new Client(this, socket);
		clientList.add(client);
	}
	
	public void handleMessage(Client client, String fromClient) {
		JSONParser parser = new JSONParser();
		try {
			
			JSONObject jObj = (JSONObject) parser.parse(fromClient);
			String resStr = new String();
			
			String action = (String) jObj.get("action");
			Message message = messageHandler.getMessageHandler(action);
			if (message != null) { // action 이 정의도어 있는 액션인 경우
				String dataStr = (String) jObj.get("data");
				JSONObject data = (JSONObject) parser.parse(dataStr) ;
				resStr = message.handleMessage(data);
			}
			else { // 정의되지 않은 양식을 보냈다면
				resStr = "err!";
			}
			System.out.println("fromClient: " + fromClient);
			System.out.println("toClient: " + resStr);
			client.send(resStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
