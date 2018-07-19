package com.clug.lunchicken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ImageServerSocket {
	
	private ServerSocket serverSocket;
    private int PORT;
    WriteBinarytoFile fw;
    
    public ImageServerSocket(int PORT) throws Exception {
    	fw = new WriteBinarytoFile();
    	this.PORT = PORT;
    }
    public void serverStart() throws IOException {
    	serverSocket = new ServerSocket();
        InetSocketAddress socketAddress = new InetSocketAddress(PORT);
        serverSocket.bind(socketAddress);
        Socket client = this.serverSocket.accept();
        Thread task = new clientThread(client);
        task.start();
    }
    private class clientThread extends Thread{
    	
    	private Socket client;
    	clientThread(Socket client) {
    		this.client=client;
    	}
    	public void run() {
    		String clientAddress = client.getInetAddress().getHostAddress();
            System.out.println("\r\nNew connection from " + clientAddress);
            
            while (true) {
                // Read binary data from client socket and write to file
                try{
    	            BufferedInputStream bis = new BufferedInputStream(client.getInputStream());        
    	            String fileName = "image-" + System.currentTimeMillis() + ".jpeg";
    	            int fileSize = fw.writeFile(fileName, bis);
    	            bis.close();
    	            System.out.println("\r\nWrote " + fileSize + " bytes to file " + fileName);
    	        } catch(IOException e) {
    	        	e.printStackTrace();
    	        } finally {
    	            // Close socket connection
    	            try {
    					client.close();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
    	        }	
            }
            
    	}
    }
    
    public InetAddress getSocketAddress() {
        return this.serverSocket.getInetAddress();
    }
    public int getPort() {
        return this.serverSocket.getLocalPort();
    }
}
