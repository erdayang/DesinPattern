package com.mu.yang.rpc.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by xuanda007 on 2017/2/16.
 */
public class GoodServer {
    private volatile boolean running = false;

    private class Listener implements Runnable{
        private ServerSocketChannel acceptChannel = null;
        private Selector selecor = null;
        private InetSocketAddress address = null;
        private Reader[] readers = null;
        public Listener(String ip, int port) throws IOException {
            address = new InetSocketAddress(ip, port);
            acceptChannel = ServerSocketChannel.open();
            acceptChannel.configureBlocking(false);
            ServerSocket socket = acceptChannel.socket();
            port = socket.getLocalPort();
            selecor = Selector.open();

        }
        synchronized Selector getSelecor(){return selecor;}
        public void doAccept(SelectionKey key) throws IOException {
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel channel;
            while((channel = server.accept())!= null){
                channel.configureBlocking(false);
                channel.socket().setTcpNoDelay(false);
                channel.socket().setKeepAlive(true);

                key.attach(channel);
            }


        }
        @Override
        public void run() {
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
                }
            }
            synchronized (this) {
                try {
                    acceptChannel.close();
                    selecor.close();;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Reader extends Thread{
        private  final Selector readSelector;
        private SocketChannel channel;
        Reader(String name, SocketChannel channel) throws IOException {
            super(name);
            readSelector = Selector.open();
            this.channel = channel;
        }

        public void doReadLoop(){
            while(running) {
                SelectionKey key = null;
                try {
                    channel.register(readSelector, SelectionKey.OP_READ);
                    readSelector.select();

                    Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
                    while(iterator.hasNext()) {
                        key = iterator.next();
                        iterator.remove();
                        if(key.isReadable()){
                            doRead(key);
                        }
                        key = null;
                    }
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        public void doRead(SelectionKey key){

        }

        public void run(){

        }

    }
}
