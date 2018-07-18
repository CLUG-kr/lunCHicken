package com.clug.lunchicken.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

public class Client implements Runnable{
	
	private Logger logger = Logger.getLogger("LoginServer");
	
	private ClientHandler handler;
	private Socket clientSocket;
	private Thread clientThraed;
	private BufferedReader reader;
	private PrintWriter writer;
	public Client(ClientHandler handler, Socket clientSocket) throws IOException {
		this.handler = handler;
		this.clientSocket = clientSocket;
		this.clientThraed = new Thread(this);
		this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
		this.clientThraed.start();
	}
	
	public Socket getClientSocket() {
		return this.clientSocket;
	}

	public Thread getClientThraed() {
		return clientThraed;
	}
	
	public void disconnect() {
		getClientThraed().interrupt();
		try {
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		writer.println(msg);
		logger.info("to "+clientSocket.getInetAddress() + ","+msg);
	}

	@Override
	public void run() {
		while(true) {
			try {
				String fromClient = reader.readLine();
				logger.info("from "+clientSocket.getInetAddress() + ","+fromClient);
				handler.handleMessage(this, fromClient);
			} catch (SocketException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				if (e.getCause().toString().equals("Stream closed")) break;
			} 
		}
		logger.info(clientSocket.getInetAddress() + "socket end");
	}
	
	/**
	 * 소켓이 열려있는 지 확인하는 메소드
	 * 참조 : http://cbts.tistory.com/124 
	 * @return
	 */
	public boolean isConnect() {
		if (clientSocket == null) return false;
		return  clientSocket.isConnected() && ! clientSocket.isClosed();
	}
	
	@SuppressWarnings("unchecked")
	public void sendNewToken(String token) {
		JSONObject resObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		resObj.put("action", "token_new");
		dataObj.put("token", token);
		resObj.put("data", dataObj);
		send(resObj.toJSONString());
	}
	
}
