package com.clug.lunchicken.login.message;

import org.json.simple.JSONObject;

import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.db.AccountBean;

/**
 * 회원가입시 주고 받는 메세지.
 * 구글 로그인 인증 후 계정 명만 인증받는다.
 * 주고 받는 데이터는 다음과 같다.<br>
 * request: {
 * 	"action":"register",
 * 	"data":{
 * 		"account_id":"(accountId)",
 * 		"account_email":"(accountEmail)"
 * 	}
 * }<br>
 * response:{
 * 	"action":"register",
 * 	"data":{
 * 		"response":"(responseCode)"
 * 	}
 * @author JoMingyu
 *
 */
public class MessageRegister extends Message{

	public MessageRegister(LoginServer loginServer) {
		super(loginServer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(JSONObject data) {
		
		String id = (String) data.get("account_id");
		String email = (String) data.get("account_email");
		AccountBean account = new AccountBean();
		account.setId(id);
		account.setEmail(email);
		
		int result = loginServer.getDatabase().getAccount().register(account);
		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "register");
		resData.put("response", String.valueOf(result));
		resObj.put("data", resData.toJSONString());
		return resObj.toJSONString();
	}

}
