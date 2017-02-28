package com.mu.yang.rpc.connector;

import com.alibaba.fastjson.JSON;
import com.mu.yang.rpc.core.Connector;
import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class SimpleConnector implements Connector, Runnable {
    private InputStream inputStream;
    private OutputStream outputStream;
    private final long id;
    Map<String, ResponseFuture> futureMap = new ConcurrentHashMap<String, ResponseFuture>();
    public SimpleConnector(int id, Socket socket){
        this.id = id;
        System.out.println("new Connector id: "+ id);
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }



    public long getId() {
        return this.id;
    }

    public ResponseFuture send(Request request) {

        System.out.println("send request: " + request);

        try {
            int length = request.toString().getBytes().length;
            ByteBuffer bytes = ByteBuffer.allocate(4 + length);
            bytes.putInt(length);
            bytes.put(request.toString().getBytes());
            outputStream.write(bytes.array());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseFuture responseFuture = new ResponseFuture(request);
        futureMap.put(request.getId(), responseFuture);
        return responseFuture;
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
        }finally {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        try {
            for(;;) {
                byte[] result = new byte[4096];
                int size = 0;
                    size = inputStream.read(result);
                String responseString = new String(result);
                System.out.println("receive response: " + responseString);
                Response response = JSON.parseObject(responseString, Response.class);
                if(null != response.getId()) {
                    ResponseFuture future = futureMap.get(response.getId());
                    if(null != future){
                        future.done(response);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
