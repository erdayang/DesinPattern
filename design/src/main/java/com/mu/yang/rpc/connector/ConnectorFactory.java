package com.mu.yang.rpc.connector;


import com.mu.yang.rpc.core.Connector;
import com.mu.yang.rpc.core.ConnectorEngine;
import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * simple connector manager,
 * 1, the strategy when creating new connector
 * 2, the strategy how to get the connector to handle the new request
 * 3, the strategy for deploying the idle connectors ???
 * Created by yangxianda on 2016/12/18.
 */
public abstract class ConnectorFactory implements ConnectorEngine{
    protected SocketFactory socketFactory = SocketFactory.getDefault();
    protected List<Connector> connectors = new ArrayList<Connector>();
    protected AtomicInteger id = new AtomicInteger();
    protected AtomicInteger ROUNDROBIN = new AtomicInteger(0);
    protected static int MAX_CONNECTOR = 1; // default is 5
    protected InetAddress serverAddress = null;
    protected static int port = 8080;

    /** may be we can use the producer-cosumer type to digest the requests*/
    private LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();
    private boolean lazyInit = true;

    static{
        MAX_CONNECTOR = 1; //  read from property
        port = 8080;
    }

    public ConnectorFactory(InetAddress address, int port){
        this.serverAddress = address;
        this.port = port;
        if(lazyInit) {
            init();
        }
    }

    public ConnectorFactory(String zookeeper, String jdns){

    }

    /**
     * init all connector before communicating
     */
    private void init(){
        while(id.intValue() < MAX_CONNECTOR){
            createConnector();
            System.out.println("connectors size: " + connectors.size());
        }
    }



    @Override
    public abstract Connector createConnector();

    @Override
    public abstract Connector chooseConnector();

    public ResponseFuture send(Request request) {
        Connector connector = chooseConnector();
        return connector.send(request);
    }

    @Override
    public void shutdown() {
        for(int i = 0; i < connectors.size(); i++){
            connectors.get(i).shutdown();
        }
    }

}
