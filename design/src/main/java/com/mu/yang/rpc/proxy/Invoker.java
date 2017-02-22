package com.mu.yang.rpc.proxy;

import com.mu.yang.rpc.connector.ResponseFuture;
import com.mu.yang.rpc.core.Connector;
import com.mu.yang.rpc.core.ConnectorEngine;
import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;
import com.mu.yang.rpc.entity.ResultCode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Invoker implements InvocationHandler {

    private final ConnectorEngine connectorEngine;

    public Invoker(ConnectorEngine engine){
        this.connectorEngine = engine;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = buildRequest(method, args);
        ResponseFuture responseFuture = connectorEngine.send(request);
        return responseFuture.get();
    }

    public Request buildRequest(Method method, Object[] args){
        Request request = new Request();
        request.setMethod(method.getName());
        request.setClazz(method.getDeclaringClass().getName());
        Class<?>[] paramTypes = new Class<?>[args.length];
        for(int i = 0; i < args.length; i++){
            Object obj = args[i];
            paramTypes[i] = (obj.getClass());
        }
        request.setParamType(paramTypes);
        request.setParams(args);
        return request;
    }
}
