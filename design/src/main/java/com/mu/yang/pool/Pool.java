package com.mu.yang.pool;

import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuanda007 on 2016/12/12.
 */
public class Pool {
    ThreadPoolExecutor poolExecutor;
    public Pool(int i){
        poolExecutor = new ThreadPoolExecutor(i,3,1000,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(2),new MyFactory("xianda"));
    }

    public class MyFactory implements ThreadFactory{
        private String name;
        private AtomicInteger count = new AtomicInteger();
        public MyFactory(String name){
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(buildName());
            return thread;
        }

        public String buildName(){
            return name +"_"+count.incrementAndGet();
        }
    }

    public void add(int n){
        for(int i = 0; i < n; i++){
            poolExecutor.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    int count = 0;
                    for(long i = 0; i < 100000000; i++){
                            count ++;
                    }
                    System.out.println(count);
                    return null;
                }
            });
        }
    }

    public void print(){
        System.out.println(poolExecutor.getQueue().size());
    }

    public static void main(String [] args){
        Pool pool = new Pool(2);
        pool.add(3);
        pool.print();

    }
}
