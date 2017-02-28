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
    public static void main(String [] args) throws IOException {
        GoodServer goodServer = new GoodServer("127.0.0.1", 8080);
    }
    public GoodServer(String ip, int port) throws IOException {

        listener = new Listener(ip, port);
        listener.start();
    }




}
