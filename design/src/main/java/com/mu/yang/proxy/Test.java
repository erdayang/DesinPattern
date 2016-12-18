package com.mu.yang.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by yangxianda on 2016/12/10.
 */
public class Test {

    public static class ProxyBuilder<T>{
        private Class<T> clazz;
        public ProxyBuilder(){
        }

        public ProxyBuilder<T> withInterface(Class<T> clazz){
            this.clazz = clazz;
            return this;
        }

        public  T build(){
            T t = (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new Invocation());

            return   t;
        }
    }

    public static void main(String [] args){

        IHelloWorld iHelloWorld = (new ProxyBuilder<IHelloWorld>().withInterface(IHelloWorld.class).build());
        System.out.println(iHelloWorld.get("here"));

    }
}
