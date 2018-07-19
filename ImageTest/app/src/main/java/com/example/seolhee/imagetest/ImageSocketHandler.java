package com.example.seolhee.imagetest;


import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class ImageSocketHandler {
    private String TAG = "ImageSocketHandler";

    private static ImageSocketHandler instance;
    private ImageSocketHandler() {}
    public synchronized static ImageSocketHandler getInstance(){
        if (instance == null) instance = new ImageSocketHandler();
        return instance;
    }

    private ImageSocket imageSocket;
    public void connectSocket(String ip, int port){
        if (imageSocket == null){
            imageSocket = new ImageSocket();
            imageSocket.setFileDir(getFileDir());
            imageSocket.connect(ip, port);
            Log.d(TAG, "init socket");
        }
        else {
            Log.d(TAG, "already connect");
        }
    }

    private File fileDir;
    public void setFileDir(File fileDir){
        this.fileDir = fileDir;
    }
    public File getFileDir(){
        return fileDir;
    }

    public void send(final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getCurrentActivity(), "handler send start" , Toast.LENGTH_SHORT).show();
                    }
                });

                while(!imageSocket.isSocketConnected()) {} // 소켓 연결 대기

                getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getCurrentActivity(), "imageSocketHandler" , Toast.LENGTH_SHORT).show();
                    }
                });
                imageSocket.send(msg);
                //Log.d(TAG, "send : " + msg);
            }
        }).start();
    }

    private HashMap<String, LinkedList<ImageSocketListener>> listeners = new HashMap<>();
    public void registerListener(String action, ImageSocketListener listener){
        if (listeners.containsKey(action)){
            listeners.get(action).add(listener);
        }
        else {
            LinkedList<ImageSocketListener> createdList = new LinkedList<>();
            createdList.add(listener);
            listeners.put(action, createdList);
        }
    }
    public void unregisterListener(String action, ImageSocketListener listener){
        if (listeners.containsKey(action)){
            listeners.get(action).remove(listener);
        }
    }
    public LinkedList<ImageSocketListener> getListeners(String action){
        return listeners.get(action);
    }

    public void handleMessage(final String rawInput){

        //Log.d(TAG, "translate it... " + rawInput);
        String action = "image";

        LinkedList<ImageSocketListener> imageSocketListeners = getListeners(action);
        if (imageSocketListeners != null){
            //Log.d(TAG, "find listener... " + rawInput);
            for (ImageSocketListener listener : imageSocketListeners){
                listener.onMessage(rawInput);
            }
        }

    }

    Activity currentActivity;
    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }
    public Activity getCurrentActivity() {
        return this.currentActivity;
    }

}
/* toast
getCurrentActivity().runOnUiThread(new Runnable() {
@Override
public void run() {
        Toast.makeText(getCurrentActivity(), "for" , Toast.LENGTH_SHORT).show();
        }
        });
*/
