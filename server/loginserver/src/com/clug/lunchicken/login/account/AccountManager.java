package com.clug.lunchicken.login.account;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.clug.lunchicken.login.Client;
import com.clug.lunchicken.login.ClientHandler;
import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.account.IAccountManager;

public class AccountManager implements IAccountManager, Runnable{

	private HashMap<String, Account> tokenMap;
	private LoginServer loginServer;
	private ClientHandler clientHandler;
	private Aes256Module encodeModule;
	private Thread newTokenThread;
	public AccountManager(LoginServer loginServer) {
		this.loginServer = loginServer;
		this.clientHandler = loginServer.getClientHandler();
		this.tokenMap = new HashMap<>();
		this.encodeModule = Aes256Module.getInstance();
		this.newTokenThread = new Thread(this);
		this.newTokenThread.start();
	}
	
	
	@Override
	public String registerAccount(String id, String token, Client client) {
		String newToken = token;
		if (tokenMap.containsKey(id)) {
			if (tokenMap.get(id).getClient().isConnect()) { // 중복 로그인 시도
				// 둘 다 연결을 끊어버림 -> 비번을 바꾸고 재로그인 시도
				tokenMap.get(id).getClient().disconnect();
				client.disconnect();
				removeAccount(id);
				return null;
			}
			else { // 재 로그인. 토큰이 멀쩡하면 그대로 사용
				newToken = tokenMap.get(id).getLoginToken();
				if (!validateToken(id, newToken)) {
					newToken = makeToken(id);	
				}
				changeAccount(id, newToken, client);
			}
		}
		else { // 중복되는 아이디가 없을 경우
			// 바로 등록시켜줌
			newToken = makeToken(id);
			addAccount(id, newToken, client);
		}
		
		return newToken;
	}

	@Override
	public boolean validateToken(String id, String token) {
		if(token == null) return false;
		try {
			String decodeToken = encodeModule.decodeAes(token);
			JSONObject data = (JSONObject) new JSONParser().parse(decodeToken.split(":")[1]);
			if (data.containsKey("account_id") && data.containsKey("created_time")) {
				if (!data.get("account_id").toString().equals(id)) return false;
				long currentTime = System.currentTimeMillis();
				long createdTime = Long.valueOf((String) data.get("created_time"));
				if (currentTime - createdTime > 30*60*1000) return false;
				return true;
			}
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | ParseException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String makeToken(String id) {
		JSONObject tokenObj = new JSONObject();
		tokenObj.put("account_id", id);
		tokenObj.put("created_time", String.valueOf(System.currentTimeMillis()));
		String originStr = tokenObj.toJSONString();
		
		StringBuilder modulationStr = 
			new StringBuilder()
			.append(System.currentTimeMillis() / 100)
			.append(":")
			.append(originStr)
			.append(":hello!my#friends!");
		String tokenStr = null;
		try {
			tokenStr = encodeModule.encodeAes(modulationStr.toString());
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return tokenStr;
	}

	@Override
	public void addAccount(String key, String loginToken, Client client) {
		Account account = new Account(loginToken, client, key);
		tokenMap.put(key, account);
	}
	
	@Override
	public void changeAccount(String key, String loginToken, Client client) {
		Account acc = tokenMap.get(key);
		acc.setClient(client);
		acc.setLoginToken(loginToken);
	}

	@Override
	public void removeAccount(String key) {
		tokenMap.remove(key);
	}


	@Override
	public void run() {
		while (true) {
			long startT = System.currentTimeMillis();
			for (Account acc : tokenMap.values()) {
				acc.addIssuedTime(1);
				if (acc.getIssuedTime() >= 30) {
					if (acc.getClient().isConnect()) {
						acc.setLoginToken(makeToken(acc.getAccountId()));
						acc.getClient().sendNewToken(acc.getLoginToken());
					}
					else {
						tokenMap.remove(acc.getAccountId());
					}
				}
			}
			long leadTime = System.currentTimeMillis() - startT;
			try {
				if (leadTime <= 60000) {
					Thread.sleep(60000 - leadTime);	
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
