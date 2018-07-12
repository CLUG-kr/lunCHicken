package com.clug.lunchicken.login.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.db.AccountBean;

/**
 * 로그인 할 때 주고 받는 데이터
 * request: {
 * 	"action":"login",
 * 	"data":{
 * 		"account_email":"(accountEmail)"
 * 	}
 * response: {
 * 	"action":"login",
 * 	"data":{
 * 		"response":"(responseCode)"
 * 	}
 * 
 * @author JoMingyu
 *
 */
public class MessageLogin extends Message {

	public MessageLogin(LoginServer loginServer) {
		super(loginServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(JSONObject data) {
		AccountBean accountBean = new AccountBean();
		accountBean.setEmail((String) data.get("account_email"));
		
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "login");
		resData.put("response", String.valueOf(loginServer.getDatabase().getAccount().login(accountBean)));
		resObj.put("data", resData);
		return resObj.toJSONString();
	}

}
