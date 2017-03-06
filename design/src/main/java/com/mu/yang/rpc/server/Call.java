package com.mu.yang.rpc.server;

import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;

/**
 * Created by yangxianda on 2017/3/4.
 */
public class Call {
    private Request request;
    private Response response;
    private Connection connection;
    public Call(){

    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
