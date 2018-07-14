package com.clug.lunchicken.login.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.db.AccountBean;
import com.clug.lunchicken.login.db.AccountDAO;

/**
 * 회원가입시 주고 받는 메세지.
 * 구글 로그인 인증 후 구글 토큰과 닉네임을 받는다.
 * 주고 받는 데이터는 다음과 같다.<br>
 * request: {
 * 	"action":"register",
 * 	"data":{
 * 		"account_id":"(accountId)",
 * 		"account_token":"(accountToken)"
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

		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "register");
		
		AccountBean accountBean = new AccountBean();
		String account_token = (String) data.get("account_token");
		int parseResult = getEmail(accountBean, account_token);
		
		if (parseResult == AccountDAO.SUCCESS_TOKEN_PARSE) { // 올바른 토큰이었으면
			// 아이디를 파싱함
			if (data.containsKey("account_id")) {
				// 아이디 정보를 넣음
				accountBean.setId((String) data.get("account_id"));
				// 로그인 서버에서 회원가입을 함
				parseResult = loginServer.getDatabase().getAccount().register(accountBean);
			}
			else { // 아이디가 파싱되지 않았을 경우
				parseResult = AccountDAO.ERR_WRONG_INFORMATION;
			}
		}
		resData.put("response", String.valueOf(parseResult));
		resObj.put("data", resData);
		return resObj.toJSONString();
	}
	
	private int getEmail(AccountBean accountBean, String token) {
		try {
			// get account data from google
			URL googleEmailUrl = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="+token);
			HttpsURLConnection conn = (HttpsURLConnection) googleEmailUrl.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder fromData = new StringBuilder();
			String readLine;
			while((readLine = br.readLine()) != null) {
				fromData.append(readLine);
			}
			br.close();
			conn.disconnect();
			
			// make json object
			JSONObject authorizedData = (JSONObject) new JSONParser().parse(fromData.toString());
			if (authorizedData.containsKey("email_verified") && authorizedData.containsKey("email")) {
				boolean isVerified = Boolean.valueOf((String)authorizedData.get("email_verified"));
				if (isVerified) {
					String email = (String) authorizedData.get("email");
					accountBean.setEmail(email);
					return AccountDAO.SUCCESS_TOKEN_PARSE;
				}
				else {
					return AccountDAO.ERR_NOT_VERIFIED;
				}
			}
			else {
				return AccountDAO.ERR_WRONG_TOKEN;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return AccountDAO.ERR_WRONG_TOKEN;
		} catch (IOException e) {
			e.printStackTrace();
			return AccountDAO.ERR_UNKNOW;
		} catch (ParseException e) {
			e.printStackTrace();
			return AccountDAO.ERR_WRONG_TOKEN;
		}
	}
}
