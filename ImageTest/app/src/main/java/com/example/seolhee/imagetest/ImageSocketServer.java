package com.example.seolhee.imagetest;

import java.io.BufferedInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ImageSocketServer {
    private ServerSocket serverSocket;
    private int PORT = 17779;
    /*
        public static void main(String[] args) throws Exception {
            ImageSocket app = new ImageSocket();
            app.listen();
        }
    */
    public ImageSocketServer() throws Exception {
        serverSocket = new ServerSocket();
        InetSocketAddress socketAddress = new InetSocketAddress(PORT);
        serverSocket.bind(socketAddress);
    }
    private void listen() throws Exception {
        WriteBinarytoFile fw = new WriteBinarytoFile();

        // Accept new client connection
        Socket client = this.serverSocket.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);

        // Read binary data from client socket and write to file
        BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
        String fileName = "image-" + System.currentTimeMillis() + ".jpeg";
        int fileSize = fw.writeFile(fileName, bis);
        bis.close();

        // Close socket connection
        client.close();
        System.out.println("\r\nWrote " + fileSize + " bytes to file " + fileName);
    }

    public InetAddress getSocketAddress() {
        return this.serverSocket.getInetAddress();
    }
    public int getPort() {
        return this.serverSocket.getLocalPort();
    }
}
