package com.mu.yang.rpc.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Server {

    private ServerSocket serverSocket = null;
    private int port = 8080;
    public Server(int port){
        this.port = port;
    }

    public void init(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("get new Connection: from: " + socket.getInetAddress() +" : " + socket.getPort());
                process(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] request = new byte[4096];
        int size = 0;
        size = inputStream.read(request);

        String requestString = new String(request);

        System.out.println("get request： " + requestString);

        OutputStream outputStream = socket.getOutputStream();

        Response response = new Response();
        response.setCode(ResultCode.KO);
        outputStream.write(response.toString().getBytes());
        outputStream.flush();
        System.out.println("send response: "+ response);
    }




    public static void main(String[] args){
        Server server = new Server(8080);
        server.init();
    }
}
