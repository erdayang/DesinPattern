package com.mu.yang.rpc.core;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Response {
    private String id;
    private int code;
    private Object result;
    private List<String> debugInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public List<String> getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(List<String> debugInfo) {
        this.debugInfo = debugInfo;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}

 interface ResultCode{
    int OK = 0;
     int KO = -1;
}
