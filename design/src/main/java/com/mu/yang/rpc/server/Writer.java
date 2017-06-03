package com.mu.yang.rpc.server;

import com.mu.yang.rpc.entity.Response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

/**
 * Created by yangxianda on 2017/3/6.
 */
public class Writer extends Thread {
    private final Selector writeSelector;
    private BlockingQueue<Call> responseQueue;
    public Writer(String name, BlockingQueue<Call> responseQueue) throws IOException {
        this.setName(name);
        this.responseQueue = responseQueue;
        writeSelector = Selector.open();
    }

    public void run(){
        System.out.println(this.getName() + "start...");
        while(true) {
            registWriters();
            try {
                int n = writeSelector.select(1000);
                if(n == 0) continue;
                Iterator<SelectionKey> it = writeSelector.selectedKeys().iterator();
                while(it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if(key.isValid() && key.isWritable()) {
                        doAsyncWrite(key);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void wakeUp(){
        writeSelector.wakeup();
    }

    public void doAsyncWrite(SelectionKey key) throws IOException {
        Call call = (Call) key.attachment();
        if(call.getConnection().channel != key.channel()){
            throw new IOException("bad channel");
        }
        byte[] aa = call.getResponse().toString().getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(aa.length);
        buffer.put(aa);
        int num = ChannelUtils.channelWrite(call.getConnection().channel, buffer);
        if(num < 0 || buffer.remaining() == 0){
            key.interestOps(0);
        }
    }
    public void registWriters() {
        Iterator<Call> it = responseQueue.iterator();
        while(it.hasNext()){
            Call call = it.next();
            System.out.println("get call request id="+call.getRequest().getId());
            it.remove();
            SelectionKey key = call.getConnection().channel.keyFor(writeSelector);
            if(null == key) {
                try {
                    call.getConnection().channel.register(writeSelector, SelectionKey.OP_WRITE, call);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            } else {
                key.interestOps(SelectionKey.OP_WRITE);
            }
        }
    }
}
