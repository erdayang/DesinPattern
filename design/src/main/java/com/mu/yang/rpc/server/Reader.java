package com.mu.yang.rpc.server;

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
    public Reader(String name) throws IOException {
        super(name);
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
                System.out.println("after select");

                Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    System.out.println("befor read");
                    if(key.isReadable()){
                        System.out.println("before read");
                        doRead(key);
                    }
                    key = null;
                }
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void doRead(SelectionKey key) throws IOException {
        System.out.println( this.getName() + "doReader");
        Connection connection = (Connection)key.attachment();
        if(connection == null){
            System.out.println("connection is null");
            return;
        }
        connection.readAndProcess();
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
