package com.clug.lunchicken.data;

import android.util.Log;

import com.clug.lunchicken.loginSocket.LoginSocketHandler;
import com.clug.lunchicken.loginSocket.LoginSocketListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginData {
    private String TAG = "LoginData";
    private LoginData(){init();}
    private static LoginData instance;
    public static LoginData getInstance(){
        if (instance == null){
            synchronized (LoginData.class){
                if (instance == null){
                    instance = new LoginData();
                }
            }
        }
        return instance;
    }

    private void init(){
        LoginSocketHandler.getInstance().registerListener("token_new", new LoginSocketListener() {
            @Override
            public void onMessage(String action, JSONObject data) {
                try {
                    setToken(data.getString("token"));
                    Log.d(TAG, "change token to " + getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private String accountId;
    private String token;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
