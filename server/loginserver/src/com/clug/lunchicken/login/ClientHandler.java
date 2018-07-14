package com.clug.lunchicken.login;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.clug.lunchicken.login.message.Message;
import com.clug.lunchicken.login.message.MessageHandler;

public class ClientHandler {

	private LoginServer loginServer;
	private List<Client> clientList;
	private MessageHandler messageHandler;
	
	public ClientHandler(LoginServer loginServer) {
		this.loginServer = loginServer;
		clientList = new LinkedList<>();
		messageHandler = new MessageHandler(loginServer);
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
				JSONObject data = (JSONObject) jObj.get("data");
				resStr = message.handleMessage(data);
			}
			else { // 정의되지 않은 양식을 보냈다면
				resStr = "err!";
			}
			//System.out.println("fromClient: " + fromClient);
			//System.out.println("toClient: " + resStr);
			client.send(resStr);
		} catch (ParseException e) {
			//e.printStackTrace();
		}
	}
	
}
