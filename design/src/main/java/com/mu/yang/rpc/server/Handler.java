package com.mu.yang.rpc.server;

import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;
import com.mu.yang.rpc.entity.ResultCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;

/**
 * 处理数据
 * Created by yangxianda on 2017/3/4.
 */
public class Handler extends Thread {

    private BlockingQueue<Call> requestQueue;
    private BlockingQueue<Call> responseQueue;
    private Writer writer;
    public Handler(BlockingQueue<Call> requestQueue, BlockingQueue<Call> responseQueue){
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    public void run(){
        while(true){
            try {
                final Call call = requestQueue.take();
                process(call);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void process(Call call){
        Request request = call.getRequest();
        Response response = processRequest(request);
        call.setResponse(response);
    }

    public Response processRequest(Request request){
        Response response = new Response();
        response.setId(request.getId());
        try {
            Object obj = InstanceMap.getInstance(request.getClazz());
            Class clazz= obj.getClass();
            Method method = clazz.getMethod(request.getMethod(), request.getParamType());
            Object result = method.invoke(obj, request.getParams());
            response.setResult(result);
            response.setCode(ResultCode.SUCCESS);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response.setCode(ResultCode.NOSUCHMETHOD);
            response.setError(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            response.setCode(ResultCode.NOSUCHMETHOD);
            response.setError(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;
    }
}
