package com.mu.yang.rpc.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Response {
    private String id;
    private ResultCode code;
    private Throwable error;
    private Object result;
    private List<String> debugInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
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

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}

