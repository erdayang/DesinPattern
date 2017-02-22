package com.mu.yang.rpc.core;


/**
 * create new conector and choose connector to send request
 * Created by yangxianda on 2016/12/18.
 */
public interface ConnectorEngine extends Connector{
    Connector createConnector();
    Connector chooseConnector();
}
