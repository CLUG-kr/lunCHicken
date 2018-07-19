package com.example.seolhee.imagetest;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ImageSocket implements Runnable{

    private static String TAG = "ImageSocket";
    private String serverIp;
    private int serverPort;

    private ImageSocketHandler imageSocketHandler;
    private Socket socket;
    private Thread thread;
    private BufferedReader socketIn;
    //private BufferedInputStream socketIn;
    private PrintWriter socketOut;

    public ImageSocket(){
        imageSocketHandler = ImageSocketHandler.getInstance();
        thread = new Thread(this);
    }

    public void connect(String ip, int port){
        this.serverIp = ip;
        this.serverPort = port;
        thread.start();
    }

    public void send(String msg){
        if(socketOut == null) {
            getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getCurrentActivity(), "socketOut is null" , Toast.LENGTH_SHORT).show();
                }
            });
            try {
                socketOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            } catch (final IOException e) {
                getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getCurrentActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        }
        socketOut.println(msg);
        socketOut.flush();
    }

    @Override
    public void run() {
        try {
            while (socket == null || !socket.isConnected()){
                socket = new Socket(serverIp, serverPort);
                socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                //socketIn = new BufferedInputStream(socket.getInputStream());
                socketOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            }
            socketIn.ready();
            Log.d(TAG, "init I/O");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "start while loop"+(socketIn==null));
        while(true){
            try{
                int i=1;
                //StringBuilder resStr = new StringBuilder();
                if (socketIn != null) {
                    String readLine = socketIn.readLine();
                    imageSocketHandler.handleMessage(readLine);
                }
            } catch (SocketException e){
                getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getCurrentActivity(), "disconnect" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private File fileDir;
    public void setFileDir(File fileDir){
        this.fileDir = fileDir;
    }

    public File getFileDir(){
        return fileDir;
    }

    private boolean isConnected;
    public boolean isSocketConnected() {
        if (socket == null) return false;
        else return socket.isConnected();
    }


    Activity currentActivity;
    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }
    public Activity getCurrentActivity() {
        return this.currentActivity;
    }

}

