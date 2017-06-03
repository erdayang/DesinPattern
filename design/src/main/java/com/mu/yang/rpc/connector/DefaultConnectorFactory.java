package com.mu.yang.rpc.connector;

import com.mu.yang.rpc.core.Connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by yangxianda on 2016/12/30.
 */
public class DefaultConnectorFactory extends ConnectorFactory {
    public DefaultConnectorFactory(InetAddress address, int port) {
        super(address, port);
    }

    @Override
    public Connector createConnector() {
        Socket socket = null;
        try {
            socket = socketFactory.createSocket(serverAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("create new client: " + socket.getPort());
        int newId = id.getAndIncrement();
        Connector connector = new SimpleConnector(newId, socket);
        connectors.add(connector);
        return connector;
    }

    @Override
    public Connector chooseConnector() {
        if(id.intValue() >= MAX_CONNECTOR){
            System.out.println("get connector from list");
            return connectors.get(ROUNDROBIN.getAndIncrement() % MAX_CONNECTOR);
        }
        return  createConnector();
    }
}
