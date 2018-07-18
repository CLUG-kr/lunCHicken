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

import com.clug.lunchicken.login.Client;
import com.clug.lunchicken.login.LoginServer;
import com.clug.lunchicken.login.db.AccountBean;
import com.clug.lunchicken.login.db.AccountDAO;

/**
 * 로그인 할 때 주고 받는 데이터
 * request: {
 * 	"action":"login",
 * 	"data":{
 * 		"account_google_token":"(accountToken)",
 * 		"account_login_token":"(accountToken)"
 * 	}<br>
 * response: {
 * 	"action":"login",
 * 	"data":{
 * 		"account_id":"(accountId)"
 * 		"account_login_token":"(accountToken)"
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

	/**
	 * 로그인 메세지를 처리하는 메소드
	 * @param data JSONObject
	 * @return
	 * ERR_NOT_VERIFIED(401) - 이메일 인증이 안되어있을 경우<br>
	 * ERR_WRONG_TOKEN(402) - 잘 못 구성된 토큰일 경우<br>
	 * 
	 * ERR_UNKNOW(1) - 통신 과정 또는 알 수 없는 곳에서 오류가 발생 했을 경우<br>
	 * 
	 * ERR_WRONG_INFORMATION(301) - 등록되지 않은 로그인 일 경우<br>
	 * SUCCESS_LOGIN(300) - 로그인을 성공했을 경우<br>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String handleMessage(Client client, JSONObject data) {

		JSONObject resObj = new JSONObject();
		JSONObject resData = new JSONObject();
		resObj.put("action", "login");
		
		AccountBean accountBean = new AccountBean();
		String accountGoogleToken = (String) data.get("account_google_token");
		int parseResult = getEmail(accountBean, accountGoogleToken);
		String accountLoginToken = (String) data.get("account_login_token");
		if (parseResult == AccountDAO.SUCCESS_TOKEN_PARSE) { // 올바른 토큰이었으면
			// 로그인 서버에서 로그인을 함
			parseResult = loginServer.getDatabase().getAccount().login(accountBean);
			if (parseResult == AccountDAO.SUCCESS_LOGIN) { // 로그인을 성공하였으면
				resData.put("account_id", accountBean.getId());
			}
		}
		
		accountLoginToken = loginServer.getAccountManager().registerAccount(accountBean.getId(), accountLoginToken, client);
		if (accountLoginToken == null) {
			
		}
		
		resData.put("response", String.valueOf(parseResult));
		resObj.put("data", resData);
		return resObj.toJSONString();
	}
	
	/**
	 * 이메일을 가져오는 메소드
	 * @param accountBean
	 * @param token
	 * @return
	 * SUCCESS_TOKEN_PARSE(400) - 토큰 분석에 성공했을 경우<br>
	 * ERR_NOT_VERIFIED(401) - 이메일 인증이 안되어있을 경우<br>
	 * ERR_WRONG_TOKEN(402) - 잘 못 구성된 토큰일 경우<br>
	 * 
	 * ERR_UNKNOW(1) - 통신 과정 또는 알 수 없는 곳에서 오류가 발생 했을 경우<br>
	 */
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
			return AccountDAO.ERR_WRONG_INFORMATION;
		}
	}

}
