package com.mu.yang.rpc.entity;

/**
* Created by xuanda007 on 2016/12/19.
*/
public enum  ResultCode{
    SUCCESS(1),EXEPTION(2),NETWORK_ERROR(3),NOSUCHMETHOD(4),NOSUCHCLASS(5);

    private int code;
    ResultCode(int i){
        this.code = i;
    }

}
