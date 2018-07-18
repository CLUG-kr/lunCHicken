package com.clug.lunchicken.loginSocket;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class LoginSocketHandler {
    private String TAG = "LoginServerHandler";
    private static LoginSocketHandler instance;
    private LoginSocketHandler() {}
    public synchronized static LoginSocketHandler getInstance(){
        if (instance == null) instance = new LoginSocketHandler();
        return instance;
    }

    private LoginSocket loginSocket;
    public void connectSocket(){
        if (loginSocket == null){
            loginSocket = new LoginSocket();
            loginSocket.connect();
            Log.d(TAG, "init socket");
        }
        else {
            Log.d(TAG, "already connect");
        }
    }

    public void send(final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginSocket.send(msg);
                Log.d(TAG, "send : " + msg);
            }
        }).start();
    }

    private HashMap<String, LinkedList<LoginSocketListener>> listeners = new HashMap<>();
    private LinkedList<LoginSocketOpenListener> openListeners = new LinkedList<>();
    private LinkedList<LoginSocketCloseListener> closeListeners = new LinkedList<>();

    public LinkedList<LoginSocketOpenListener> getOpenListeners() {
        return openListeners;
    }
    public void registerOpenListener(LoginSocketOpenListener listener){
        openListeners.add(listener);
    }
    public void unregisterOpenListener(LoginSocketOpenListener listener){
        openListeners.remove(listener);
    }
    public LinkedList<LoginSocketCloseListener> getCloseListeners() {
        return closeListeners;
    }
    public void registerCloseListener(LoginSocketCloseListener listener){
        closeListeners.add(listener);
    }
    public void unregisterCloseListener(LoginSocketCloseListener listener){
        closeListeners.remove(listener);
    }

    public void registerListener(String action, LoginSocketListener listener){
        if (listeners.containsKey(action)){
            listeners.get(action).add(listener);
        }
        else {
            LinkedList<LoginSocketListener> createdList = new LinkedList<>();
            createdList.add(listener);
            listeners.put(action, createdList);
        }
    }
    public void unregisterListener(String action, LoginSocketListener listener){
        if (listeners.containsKey(action)){
            listeners.get(action).remove(listener);
        }
    }
    public LinkedList<LoginSocketListener> getListeners(String action){
        return listeners.get(action);
    }

    public void handleMessage(String rawInput){
        Log.d(TAG, "translate it... " + rawInput);
        try {
            JSONObject resObj = new JSONObject(rawInput);
            String action = resObj.getString("action");
            JSONObject data = resObj.getJSONObject("data");
            LinkedList<LoginSocketListener> loginSocketListeners = getListeners(action);
            if (loginSocketListeners != null){
                Log.d(TAG, "find listener... " + rawInput);
                for (LoginSocketListener listener : loginSocketListeners){
                    listener.onMessage(action, data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
