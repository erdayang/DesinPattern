package com.mu.yang.rpc.server;

import com.mu.yang.rpc.entity.Request;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 读数据,解码
 * Created by yangxianda on 2017/2/28.
 */
public class Reader extends Thread{
    private  final Selector readSelector;
    private BlockingQueue<Connection> connections;
    private BlockingQueue<Call> requestQueue;
    public Reader(String name, BlockingQueue<Call> requestQueue) throws IOException {
        super(name);
        this.requestQueue = requestQueue;
        readSelector = Selector.open();
        connections = new LinkedBlockingQueue<Connection>();
    }

    public void addConnection(Connection connection) throws InterruptedException {
        connections.put(connection);
        readSelector.wakeup();
    }

    public void doReadLoop(){
        System.out.println(this.getName() + "start...");
        while(true) {
            SelectionKey key = null;
            try {
                int size = connections.size();
                for(int i = size; i > 0; i--){
                    Connection connection = connections.take();
                    connection.channel.register(readSelector, SelectionKey.OP_READ, connection);
                    System.out.println(this.getName()+ ": register read selector..." + connections.size());
                }
                readSelector.select();

                Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if(key.isReadable()){
                        doRead(key);
                    }
                }
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                if(null != key)
                    key.cancel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void doRead(SelectionKey key) throws IOException {
        Connection connection = (Connection)key.attachment();
        if(connection == null){
            System.out.println("connection is null");
            return;
        }
        Request request = connection.readAndProcess();
        Call call = new Call();
        call.setConnection(connection);
        call.setRequest(request);
        try {
            requestQueue.put(call);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            doReadLoop();
        } finally {
            try {
                readSelector.close();
            } catch (IOException ioe) {
                System.err.println("Error closing read selector in " + Thread.currentThread().getName());
            }
        }
    }

}
