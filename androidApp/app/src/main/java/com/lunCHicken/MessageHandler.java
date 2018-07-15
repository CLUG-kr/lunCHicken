package com.lunCHicken;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler {

    public static final int
            ERR_UNKNOW = 1,

            SUCCESS_DUPLICATION_CHECK = 100,
            ERR_DUPLICATED_ID = 101,
            ERR_DUPLICATED_EMAIl = 102,

            SUCCESS_REGISTER = 200,

            SUCCESS_LOGIN = 300,
            ERR_WRONG_INFORMATION = 301,

            SUCCESS_TOKEN_PARSE = 400,
            ERR_NOT_VERIFIED = 401,
            ERR_WRONG_TOKEN = 402;

    private static final String TAG = "MessageHandler";

    private Activity currentActivity;

    public MessageHandler() {

    }

    /* client->server login data form
    * {
    * "action":"login",
    * "data":{
    *       "account_token":"(accountToken)"
    *       }
    } */
    public JSONObject generateLoginSendData(String idToken) {
        JSONObject sendLoginData = null;
        JSONObject tokenData = null;
        try {
            sendLoginData = new JSONObject();
            tokenData = new JSONObject();
            sendLoginData.put("action", "login");
            tokenData.put("account_token", idToken);
            sendLoginData.put("data", tokenData);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return sendLoginData;
    }

    /* client->server register data form
    * {
    * "action":"register",
    *    "data":{
    *       "account_id":"(accountId)",
    *       "account_token":"(accountToken)"
    *    }
    } */
    public JSONObject generateRegisterSendData(String idToken, String account_id) {
        JSONObject sendRegisterData = null;
        JSONObject data = null;
        try {
            sendRegisterData = new JSONObject();
            data = new JSONObject();
            sendRegisterData.put("action", "register");
            data.put("account_id", account_id);
            data.put("account_token", idToken);
            sendRegisterData.put("data", data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return sendRegisterData;
    }

    public void parseReceivedData(String receivedStr, Activity activity) {
        this.currentActivity = activity;

        JSONObject receivedJson;
        JSONObject data;
        String action;
        try {
            receivedJson = new JSONObject(receivedStr);
            action = receivedJson.getString("action");
            data = receivedJson.getJSONObject("data");
            if(action.equals("login")) {
                handleLoginData(data);
            } else if(action.equals("register")) {
                handleRegisterData(data);
            } else {
                Log.w(TAG, "paseJSONfromServer: " + "cannot resolve 'action' symbol");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 로그인 처리
     * SUCCESS_LOGIN(300) - 로그인을 성공했을 경우<br>
     * ERR_WRONG_INFORMATION(301) - 등록되지 않은 로그인 일 경우<br>
     *
     * SUCCESS_TOKEN_PARSE(400) - 토큰 분석에 성공했을 경우<br>
     * ERR_NOT_VERIFIED(401) - 이메일 인증이 안되어있을 경우<br>
     * ERR_WRONG_TOKEN(402) - 잘 못 구성된 토큰일 경우<br>
     *
     * ERR_UNKNOW(1) - 통신 과정 또는 알 수 없는 곳에서 오류가 발생 했을 경우<br>
     */
     /* {
     *    "action":"login",
     *    "data":{
     *       "account_id":"(accountId)"
     *       "response":"(responseCode)"
     *    }
     } */
    public void handleLoginData(JSONObject data) throws JSONException {

        String account_id;
        int responseCode;
        responseCode = data.getInt("response");
        switch (responseCode) {
            case SUCCESS_LOGIN:
                account_id = data.getString("account_id");
                Intent loginIntent = new Intent(currentActivity, LobbyActivity.class);
                loginIntent.putExtra("account_id", account_id);
                currentActivity.startActivity(loginIntent);
                break;
            case ERR_WRONG_INFORMATION:
                Log.d(TAG, "parseJSONfromServer: " + "not registered");
                Message failMsg = failHandler.obtainMessage();
                failHandler.sendMessage(failMsg);
                break;
        }

    }

    /**
     * 회원가입 처리
     * @param data JSONObject
     * @return
     * SUCCESS_TOKEN_PARSE(400) - 토큰 분석에 성공했을 경우<br>
     * ERR_NOT_VERIFIED(401) - 이메일 인증이 안되어있을 경우<br>
     * ERR_WRONG_TOKEN(402) - 잘 못 구성된 토큰일 경우<br>
     *
     * ERR_UNKNOW(1) - 통신 과정 또는 알 수 없는 곳에서 오류가 발생 했을 경우<br>
     *
     * ERR_WRONG_INFORMATION(301) - JSON 형식에 지켜지지 않았을 경우(id가 없거나 해서)<br>
     * ERR_DUPLICATED_ID(101) - 아이디 중복<br>
     * ERR_DUPLICATED_EMAIl(102) - 이메일 중복<br>
     * SUCCESS_REGISTER(200) - 회원가입 성공,
     */
     /* {
      *    "action":"register",
      *    "data":{
      *       "response":"(responseCode)"
      *    }
      }*/
    public void handleRegisterData(JSONObject data) throws JSONException {

        int responseCode;
        responseCode = data.getInt("response");
        switch (responseCode) {
            case SUCCESS_LOGIN:
                /*
                account_id = data.getString("account_id");
                Intent loginIntent = new Intent(currentActivity, LobbyActivity.class);
                loginIntent.putExtra("account_id", account_id);
                currentActivity.startActivity(loginIntent);
                */
                break;
            case ERR_WRONG_INFORMATION:
                /*
                Log.d(TAG, "parseJSONfromServer: " + "not registered");
                Message
                failMsg = failHandler.obtainMessage();
                failHandler.sendMessage(failMsg);
                */
                break;
        }
    }

    private Handler failHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(currentActivity.getApplicationContext(), "가입되지 않은 유저입니다." + "", Toast.LENGTH_LONG).show();
        }
    };
}
