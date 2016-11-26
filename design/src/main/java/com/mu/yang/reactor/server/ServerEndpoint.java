package com.mu.yang.reactor.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by xuanda007 on 2016/9/3.
 */
public class ServerEndpoint {
    private ServerSocketChannel serverSock = null;
    private int port = 8080;
    public ServerEndpoint(){}

    public ServerEndpoint withPort(int port) {
        this.port = port;
        return this;
    }

    public void listen() throws IOException {
        serverSock = ServerSocketChannel.open();
        serverSock.socket().bind(new InetSocketAddress(port));
        serverSock.configureBlocking(false);
        Selector selector = Selector.open();
        while(true){
            SocketChannel socketChannel = serverSock.accept();
            if(socketChannel != null){
                System.out.println("new connection: " + socketChannel.getRemoteAddress());
                ByteBuffer buffer = ByteBuffer.allocate(100);
                socketChannel.read(buffer);
                System.out.println(new String(buffer.array()));
            }
        }
    }

    public static void main(String [] args) throws IOException {
        new ServerEndpoint().withPort(8080).listen();
    }


}
