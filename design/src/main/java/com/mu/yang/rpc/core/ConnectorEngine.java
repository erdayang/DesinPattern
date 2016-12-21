package com.mu.yang.rpc.core;

import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;

/**
 * Created by yangxianda on 2016/12/18.
 */
public interface ConnectorEngine {
    Connector getConnector();
    Response send(Request request);
    void shutdown();
}
