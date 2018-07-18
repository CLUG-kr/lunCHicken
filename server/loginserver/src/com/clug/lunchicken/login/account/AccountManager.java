package com.clug.lunchicken.login.account;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

public class AccountManager implements IAccountManager{

	private HashMap<String, Client> tokenMap;
	private LoginServer loginServer;
	private ClientHandler clientHandler;
	private Aes256Module encodeModule;
	public AccountManager(LoginServer loginServer) {
		this.loginServer = loginServer;
		this.clientHandler = loginServer.getClientHandler();
		this.tokenMap = new HashMap<>();
		this.encodeModule = Aes256Module.getInstance();
	}
	
	
	@Override
	public String registerAccount(String id, String token, Client client) {
		String newToken = token;
		if (newToken == null) { // 토큰이 없다면
			newToken = makeToken(id);
			if (newToken == null) { // 혹시 토큰이 안만들어 졌을 경우를 대비한 조건문
				System.out.println("ERR - 토큰이 만들어지지 않았습니다.");
				return null;
			}
		}
		else { // 토큰이 있는데 만료되었을 경우 다시 토큰을 발급해줌
			if (!validateToken(id, newToken)) {
				newToken = makeToken(id);
				removeToken(newToken);
			}
		}
		
		tokenMap.put(newToken, client);
		
		return newToken;
	}

	@Override
	public boolean validateToken(String id, String token) {
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
	public void removeToken(String key) {
		tokenMap.remove(key);
	}
	
}
