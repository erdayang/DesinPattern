package com.mu.yang.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by yangxianda on 2016/12/10.
 */
public class ProxyHandler implements InvocationHandler {


    /**
     * 这里是怎么实现的，可以不关心
     * 1， 可以传入实现类
     * 2， 可以没有实现类，根据需要在这里操作
     * 3， 可以通过网络调用，在别的地方做出处理，然后再返回结果
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("method name； " + method.getName());

        for(Object arg : args){
            System.out.println("arg: " + arg);
        }

        return "there";
    }
}
