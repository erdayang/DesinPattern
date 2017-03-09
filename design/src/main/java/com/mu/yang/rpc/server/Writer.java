package com.mu.yang.rpc.server;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
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
        writeSelector = Selector.open();
        this.responseQueue = responseQueue;
    }

    public void run(){
        while(true){
            registerWriter();
            try {
                int n = writeSelector.select(1000);
                if(0 == n) continue;
                Iterator<SelectionKey> it = writeSelector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    if(key.isValid() && key.isWritable()){

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerWriter(){
        Iterator<Call> it = responseQueue.iterator();
        while(it.hasNext()) {
            Call call = it.next();
            it.remove();
            SelectionKey key = call.getConnection().channel.keyFor(writeSelector);

            if(null == key){
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
    private void doAsyncWrite(SelectionKey key) throws IOException {
        Call call = (Call) key.attachment();
        if(call.getConnection().channel != key.channel()) {
            throw new IOException("bad channel");
        }
        int numBytes = ChannelUtils.channelWrite(call.getConnection().channel, call.g);
        if(numBytes < 0 || call.response.remaining() == 0) {
            try {
                key.interestOps(0);
            } catch (CancelledKeyException e) {
                e.printStackTrace();
            }
        }
    }
}
