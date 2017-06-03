package com.mu.yang.rpc.server;

import com.mu.yang.rpc.entity.Request;
import com.mu.yang.rpc.entity.Response;
import com.mu.yang.rpc.entity.ResultCode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 处理数据
 * Created by yangxianda on 2017/3/4.
 */
public class Handler extends Thread {

    private BlockingQueue<Call> requestQueue;
    private BlockingQueue<Call> responseQueue;
    private Writer writer;
    public Handler(String name, BlockingQueue<Call> requestQueue){
        super.setName(name);
        this.requestQueue = requestQueue;
        this.responseQueue = new LinkedBlockingDeque<Call>();
        try {
            writer = new Writer(name+"WriterThread", responseQueue);
          //  writer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        System.out.println(this.getName() + "start...");
        while(true){
            try {
                final Call call = requestQueue.take();
                System.out.println(this.getName() + " process call:" + call.getRequest().getId());
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
      //  responseQueue.add(call);
        byte[] aa = call.getResponse().toString().getBytes();
        System.out.println(aa.length);
        ByteBuffer buffer = ByteBuffer.allocate(4+aa.length);
        buffer.putInt(aa.length);
        buffer.put(aa);
        buffer.flip();
        try {
            int num = ChannelUtils.channelWrite(call.getConnection().channel, buffer);
            System.out.println(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("response id=" + request.getId());
    //    writer.wakeUp();
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
        System.out.println("create response done");
        return response;
    }
}
