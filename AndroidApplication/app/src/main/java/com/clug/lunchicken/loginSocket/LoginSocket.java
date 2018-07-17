package com.clug.lunchicken.loginSocket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginSocket implements Runnable {
    private static String TAG = "LoginSocket";
    private static final String serverIp = "owlsogul.iptime.org";
    private static final int serverPort = 17777;

    private LoginSocketHandler loginSocketHandler;
    private Socket socket;
    private Thread thread;
    private BufferedReader socketIn;
    private PrintWriter socketOut;

    public LoginSocket(){
        loginSocketHandler = LoginSocketHandler.getInstance();
        thread = new Thread(this);
    }

    public void connect(){
        thread.start();
    }

    public void send(String msg){
        if (socketOut == null) return;
        socketOut.println(msg);
    }



    @Override
    public void run() {
        try {
            while (socket == null){
                socket = new Socket(serverIp, serverPort);
            }
            while (socketIn == null || socketOut == null){
                socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
                //StringBuilder resStr = new StringBuilder();
                if (socketIn != null) {
                    String readLine = socketIn.readLine();
                    loginSocketHandler.handleMessage(readLine);
                }
                    /*while((readLine = socketIn.readLine())  != null && readLine.length() > 0){
                        Log.d(TAG, "my line is " + readLine);
                        resStr.append(readLine);
                    }
                    if (resStr.length() != 0) {
                        Log.d(TAG, "okay! done! send it");
                        loginSocketHandler.handleMessage(resStr.toString());
                        Log.d(TAG, "send finish");
                    }
                    else {
                        Log.d(TAG, "something wrong");
                    }*/
            } catch (NullPointerException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }
}

