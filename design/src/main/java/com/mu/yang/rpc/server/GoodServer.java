package com.mu.yang.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuanda007 on 2017/2/16.
 */
public class GoodServer {
    private volatile boolean running = true;
    private Listener listener = null;
    private int READER_COUNT = 3;
    private Reader[] readers = null;
    public static void main(String [] args) throws IOException {
        GoodServer goodServer = new GoodServer("127.0.0.1", 8080);
    }
    public GoodServer(String ip, int port) throws IOException {
        readers = new Reader[READER_COUNT];
        for(int i = 0; i < READER_COUNT; i++){
            readers[i] = new Reader("ReaderThread-"+ i);
            readers[i].start();
        }

        listener = new Listener(ip, port);
        listener.start();
    }

    private class Listener extends Thread{
        private ServerSocketChannel acceptChannel = null;
        private Selector selecor = null;
        private InetSocketAddress address = null;
        private volatile AtomicInteger READER_INDEX = new AtomicInteger(0);
        public Listener(String ip, int port) throws IOException {
            super("Thread-listener");
            address = new InetSocketAddress(ip, port);
            acceptChannel = ServerSocketChannel.open();
            acceptChannel.configureBlocking(false);
            ServerSocket socket = acceptChannel.socket();
            socket.bind(address);
            port = socket.getLocalPort();
            selecor = Selector.open();
            acceptChannel.register(selecor, SelectionKey.OP_ACCEPT);

        }

        synchronized Selector getSelecor(){return selecor;}

        public Reader getReader(){
            return readers[READER_INDEX.getAndIncrement()%readers.length];
        }

        public void doAccept(SelectionKey key) throws IOException, InterruptedException {
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel channel;
            while((channel = server.accept())!= null){
                channel.configureBlocking(false);
                channel.socket().setTcpNoDelay(false);
                channel.socket().setKeepAlive(true);
                Connection connection = new Connection(channel);
                key.attach(connection);
                Reader reader = getReader();
                reader.addConnection(connection);
            }
        }
        @Override
        public void run() {
            System.out.println("Listener start...");
            while(running){
                SelectionKey key = null;
                try {
                    getSelecor().select();
                    Iterator<SelectionKey> iterator = getSelecor().selectedKeys().iterator();
                    while(iterator.hasNext()){
                        key = iterator.next();
                        iterator.remove();
                        if(key.isValid()){
                            if(key.isAcceptable()){
                                doAccept(key);
                            }
                        }
                        key = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (this) {
                try {
                    acceptChannel.close();
                    selecor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Reader extends Thread{
        private  final Selector readSelector;
        private BlockingQueue<Connection> connections;
        Reader(String name) throws IOException {
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
            while(running) {
                SelectionKey key = null;
                try {
                    int size = connections.size();
                    for(int i = size; i >= 0; i--){
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
}
