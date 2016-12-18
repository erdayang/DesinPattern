package com.mu.yang.rpc.core;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Request implements Serializable{
    private String id;
    private String clazz;
    private String method;
    private Object[] params;
    private boolean isDebug = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
