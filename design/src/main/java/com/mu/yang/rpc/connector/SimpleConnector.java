package com.mu.yang.rpc.connector;

import com.alibaba.fastjson.JSON;
import com.mu.yang.rpc.core.Connector;
import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;

import java.io.*;
import java.net.Socket;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class SimpleConnector implements Connector {
    private InputStream inputStream;
    private OutputStream outputStream;
    private final long id;
    public SimpleConnector(int id, Socket socket){
        this.id = id;
        System.out.println("new Connector id: "+ id);
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return this.id;
    }

    public Response send(Request request) {

        System.out.println("send request: " + request);

        try {
            outputStream.write(request.toString().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] result = new byte[4096];
        int size = 0;
        try {
            size = inputStream.read(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String responseString = new String(result);
        System.out.println("receive response: "+ responseString);
        return JSON.parseObject(responseString, Response.class);
    }

    @Override
    public void shutdown() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
