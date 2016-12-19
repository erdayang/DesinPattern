package com.mu.yang.rpc.core;

import com.alibaba.fastjson.JSON;
import com.mu.yang.proxy.HelloWorld;
import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;
import com.mu.yang.rpc.entity.ResultCode;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Server {

    private ServerSocket serverSocket = null;
    private int port = 8080;
    private Map<String, Object> objectMap = new HashMap<String, Object>();
    public Server(int port){
        this.port = port;
    }

    public Server addClass(Class<?> clazz){
        Class<?>[] objs = clazz.getInterfaces();
        try {
            for(Class<?> inter : objs){
                    objectMap.put(inter.getName(), clazz.newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
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
        byte[] requestBytes = new byte[4096];
        int size = 0;
        size = inputStream.read(requestBytes);

        String requestString = new String(requestBytes);

        System.out.println("get request： " + requestString);

        Request request = JSON.parseObject(requestString, Request.class);

        Response response = process(request);
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(response.toString().getBytes());
        outputStream.flush();
        System.out.println("send response: "+ response);
    }

    public Response process(Request request){
        Response response = new Response();
        response.setId(request.getId());
        try {
            Class clazz = Class.forName(request.getClazz());
            Object obj = objectMap.get(request.getClazz());

            Method method = clazz.getMethod(request.getMethod(), request.getParamType());
            Object result = method.invoke(obj, request.getParams());
            response.setResult(result);
            response.setCode(ResultCode.SUCCESS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setCode(ResultCode.NOSUCHCLASS);
            response.setError(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response.setCode(ResultCode.NOSUCHMETHOD);
            response.setError(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            response.setCode(ResultCode.NOSUCHMETHOD);
            response.setError(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;
    }




    public static void main(String[] args){
        Server server = new Server(8080);
        server.addClass(HelloWorld.class).init();
    }
}
