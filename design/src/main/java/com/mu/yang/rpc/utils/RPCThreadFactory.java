package com.mu.yang.rpc.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xuanda007 on 2017/1/3.
 */
public class RPCThreadFactory implements java.util.concurrent.ThreadFactory {
    private AtomicLong id = new AtomicLong(0);
    private String name = "RPC-SERVER";
    public RPCThreadFactory(){}
    public RPCThreadFactory(String factoryName){
        this.name = factoryName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(name + "-" + id.incrementAndGet());
        return thread;
    }
}
