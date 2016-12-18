package com.mu.yang.rpc.core;

/**
 * Created by yangxianda on 2016/12/18.
 */
public interface Connector {
    Response send(Request request);
}
