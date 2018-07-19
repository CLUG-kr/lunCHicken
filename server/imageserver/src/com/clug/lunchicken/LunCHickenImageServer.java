package com.clug.lunchicken;

public class LunCHickenImageServer {
	static int PORT = 20000;
	public static void main(String[] args) {
		try {
			ImageServerSocket imageServerSocket = new ImageServerSocket(PORT);
			imageServerSocket.serverStart();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
