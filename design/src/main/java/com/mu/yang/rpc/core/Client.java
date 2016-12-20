package com.mu.yang.rpc.core;

import com.mu.yang.proxy.IHelloWorld;
import com.mu.yang.rpc.proxy.Invoker;
import com.mu.yang.rpc.connector.ConnectorFactory;
import com.mu.yang.rpc.utils.NetUtils;
import com.mu.yang.utils.TimeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by yangxianda on 2016/12/18.
 */
public class Client {

    ConnectorFactory factory = ConnectorFactory.getInstance(NetUtils.getInetAddress("127.0.0.1"));
    public Client(){

    }

    public <T> ProxyBuilder<T> proxyBuilder(Class<T> clazz){
        return new ProxyBuilder<T>();
    }


    public class ProxyBuilder<T>{
        private Class<T> clazz;
        private InvocationHandler invocation;
        public ProxyBuilder(){
            this.clazz = clazz;
            invocation = new Invoker(factory);
        }

        public ProxyBuilder<T> withInterface(Class<T> clazz){
            this.clazz = clazz;
            return this;
        }

        public  T build(){
            T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, invocation);

            return   t;
        }
    }

    public static void main(String [] args){
        Client client = new Client();

        IHelloWorld helloWorld = client.proxyBuilder(IHelloWorld.class).withInterface(IHelloWorld.class).build();
        long allTtime = 0;
        for(int i = 0; i < 100; i ++){
            long begin = TimeUtil.now();
            System.out.println(helloWorld.get("here"));
            long end = TimeUtil.now() ;
            System.out.println("consume: " + (end - begin));
            allTtime+=(end - begin);
        }
        System.out.println("all consume: " + allTtime);
        System.out.println("average consume: " + allTtime/10);
    }
}
