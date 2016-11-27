package com.mu.yang.filter;

/**
 * Created by xuanda007 on 2016/11/26.
 */
public abstract class AbstractWorker implements Worker {

    public abstract void doWork(Context context);

    public final void work(Context context, WorkerChain workerChain){
        doWork(context);
        workerChain.start(context);
    }
}
