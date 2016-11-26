package com.mu.yang.reactor.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by xuanda007 on 2016/9/4.
 */
public class ClientEndpoint {
    private int serverPort = 8080;

    private String serverAddress = "127.0.0.1";

    private SocketChannel socketChannel = null;

    public ClientEndpoint withPort(int port){
        this.serverPort = port;
        return this;
    }

    public ClientEndpoint withAddress(String address){
        this.serverAddress = address;
        return this;
    }

    public void accept() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(serverAddress, serverPort));
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put("hello".getBytes());
        buffer.flip();
        while(buffer.hasRemaining())
            socketChannel.write(buffer);
    }

    public static void main(String [] args) throws IOException {
        for(int i = 0 ; i < 10; i++)
        new ClientEndpoint().withAddress("localhost")
                .withPort(8080)
                .accept();
    }
}
