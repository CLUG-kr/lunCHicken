package com.clug.lunchicken.loginSocket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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


    boolean threadRunning = true;

    @Override
    public void run() {
        while (threadRunning) {
            // connect to server
            while (socket == null) {
                try {
                    socket = new Socket(serverIp, serverPort);
                    socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    socketOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                    socketIn.ready();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (LoginSocketOpenListener listener : loginSocketHandler.getOpenListeners()) {
                listener.onOpen();
            }
            Log.d(TAG, "init I/O");


            Log.d(TAG, "start while loop" + (socketIn == null));
            while (true) {
                try {
                    if (socketIn != null) {
                        String readLine = socketIn.readLine();
                        loginSocketHandler.handleMessage(readLine);
                    }
                } catch (SocketException e){
                    for (LoginSocketCloseListener listener : loginSocketHandler.getCloseListeners()){
                        listener.onClose();
                    }
                    e.printStackTrace();
                    break;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

