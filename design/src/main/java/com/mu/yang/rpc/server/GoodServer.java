package com.mu.yang.rpc.server;

import com.mu.yang.proxy.HelloWorld;

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
 * 第一步： 实现 简单的通信， 要有压测数据
 * 第二步： 实现编码解码接口，支持多种协议
 * 第三步:  服务发现
 *
 * Created by xuanda007 on 2017/2/16.
 */
public class GoodServer {
    private volatile boolean running = true;
    private Listener listener = null;
    // 收到消息之后的队列，给予 handler处理
    private BlockingQueue requestQueue = new LinkedBlockingQueue();
    // 消息处理后的队列，用于返回
 //   private BlockingQueue responseQueue = new LinkedBlockingQueue();

    private Handler[] handlers;
    private int HANDLER_COUNT = 10;
    public static void main(String [] args) throws IOException {
        GoodServer goodServer = new GoodServer("127.0.0.1", 8080);
    }
    public GoodServer(String ip, int port) throws IOException {
        InstanceMap.addClass(HelloWorld.class);
        initHandler();
        listener = new Listener(ip, port, requestQueue);
        listener.start();
    }

    public void initHandler(){
        handlers = new Handler[HANDLER_COUNT];
        for(int i = 0; i < HANDLER_COUNT; i++){
            handlers[i] = new Handler("HandlerThread-"+i, requestQueue);
            handlers[i].start();
        }
    }





}
