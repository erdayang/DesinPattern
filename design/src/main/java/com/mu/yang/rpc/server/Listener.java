package com.mu.yang.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监听客户端的连接
 * Created by yangxianda on 2017/2/28.
 */
public class Listener extends Thread{
    private ServerSocketChannel acceptChannel = null;
    private Selector selecor = null;
    private InetSocketAddress address = null;
    private volatile AtomicInteger READER_INDEX = new AtomicInteger(0);
    private Reader[] readers = null;
    private int READER_COUNT = 3;
    private BlockingQueue<Call> requestQueue;
    public Listener(String ip, int port, BlockingQueue<Call> requestQueue) throws IOException {
        super("Thread-listener");
        this.requestQueue = requestQueue;
        address = new InetSocketAddress(ip, port);
        acceptChannel = ServerSocketChannel.open();
        acceptChannel.configureBlocking(false);
        ServerSocket socket = acceptChannel.socket();
        socket.bind(address);
        port = socket.getLocalPort();
        selecor = Selector.open();
        acceptChannel.register(selecor, SelectionKey.OP_ACCEPT);
        readers = new Reader[READER_COUNT];
        for(int i = 0; i < READER_COUNT; i++){
            readers[i] = new Reader("ReaderThread-"+ i, requestQueue );
            readers[i].start();
        }

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
        while(true){
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
    }
}
