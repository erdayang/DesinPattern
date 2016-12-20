package com.mu.yang.rpc.connector;


import com.mu.yang.rpc.core.Connector;
import com.mu.yang.rpc.core.ConnectorEngine;
import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class ConnectorFactory implements ConnectorEngine{
    private SocketFactory socketFactory = SocketFactory.getDefault();
    private List<Connector> connectors = new ArrayList<Connector>();
    private AtomicInteger id = new AtomicInteger();
    private AtomicInteger ROUNDROBIN = new AtomicInteger(0);
    private static int MAX_CONNECTOR = 4; // default is 5
    private InetAddress serverAddress = null;
    private static int port = 8080;
    private LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    static{
        MAX_CONNECTOR = 2; //  read from property
        port = 8080;
    }

    private static ConnectorFactory instance = null;
    private ConnectorFactory(InetAddress address){
        this.serverAddress = address;
        init();
    }
    public static ConnectorFactory getInstance(InetAddress address){
        synchronized (ConnectorFactory.class){
            if(null == instance){
                synchronized (ConnectorFactory.class){
                    instance = new ConnectorFactory(address);
                }
            }
        }
        return instance;
    }

    private void init(){
        while(id.intValue() < MAX_CONNECTOR){
            Socket socket = null;
            try {
                socket = socketFactory.createSocket(serverAddress, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("create new client: " + socket.getPort());
            int newId = id.getAndIncrement();
            Connector connector = new SimpleConnector(newId, socket);
            connectors.add(connector);
            System.out.println("connectors size: " + connectors.size());
        }
    }


    public Connector getConnector(){
        if(id.intValue() >= MAX_CONNECTOR){
            System.out.println("get connector from list");
            return connectors.get(ROUNDROBIN.getAndIncrement() % MAX_CONNECTOR);
        }
        Socket socket = null;
        try {
            socket = socketFactory.createSocket(serverAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("create new client: " + socket.getPort());
        int newId = id.getAndIncrement();
        Connector connector = new SimpleConnector(newId, socket);
        connectors.add(connector);
        System.out.println("connectors size: " + connectors.size());

        return connector;
    }

    public void send(Request request) {
        queue.offer(request);
    }

    private class MessageHandler implements Runnable{

        @Override
        public void run() {
            Request request = null;
            try {
                request = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Connector connector = getConnector();
            connector.send(request);
        }
    }



    public static void main(String [] args) throws UnknownHostException {
        ConnectorFactory factory = ConnectorFactory.getInstance(InetAddress.getLocalHost());
        for(int i = 0; i < 5; i ++) {
            Connector connector = factory.getConnector();

            Request request = new Request();
            request.setId(UUID.randomUUID().toString());

            Response response = connector.send(request);

        }
    }


}
