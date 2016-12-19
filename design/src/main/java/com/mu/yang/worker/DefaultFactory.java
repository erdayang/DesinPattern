package com.mu.yang.worker;

import com.mu.yang.worker.example.ApplicationWorkerChain;

/**
 * Created by xuanda007 on 2016/12/8.
 */
public class DefaultFactory implements Factory{
    WorkerChain workerChain = null;

    public DefaultFactory(){
        this.workerChain = new ApplicationWorkerChain();
    }

    public DefaultFactory(WorkerChain workerChain){
        this.workerChain = workerChain;
    }

    @Override
    public void produce() {
    }
}
