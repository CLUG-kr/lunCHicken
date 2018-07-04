package com.clug.lunchicken.login.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.db.AccountBean;

public class MessageRegister extends Message{

	public MessageRegister(LoginServer loginServer) {
		super(loginServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(JSONObject data) {
		
		String id = (String) data.get("account_id");
		String pw = (String) data.get("account_pw");
		String email = (String) data.get("account_email");
		AccountBean account = new AccountBean();
		account.setId(id);
		account.setPw(pw);
		account.setEmail(email);
		
		int result = loginServer.getDatabase().getAccount().register(account);
		JSONObject resObj = new JSONObject();
		resObj.put("action", "register");
		JSONObject dataObj = new JSONObject();
		dataObj.put("response", String.valueOf(result));
		resObj.put("data", dataObj.toJSONString());
		return resObj.toJSONString();
	}

}
