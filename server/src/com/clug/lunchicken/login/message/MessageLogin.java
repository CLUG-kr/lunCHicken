package com.clug.lunchicken.login.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.db.AccountBean;

public class MessageLogin extends Message {

	public MessageLogin(LoginServer loginServer) {
		super(loginServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(JSONObject data) {
		AccountBean accountBean = new AccountBean();
		accountBean.setEmail((String) data.get("account_email"));
		
		JSONObject response = new JSONObject();
		response.put("action", "login");
		JSONObject resData = new JSONObject();
		resData.put("response", String.valueOf(loginServer.getDatabase().getAccount().login(accountBean)));
		response.put("data", resData);
		return response.toJSONString();
	}

}
