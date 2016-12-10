package com.mu.yang.worker;

/**
 * Created by xuanda007 on 2016/11/26.
 */
public abstract class AbstractWorker implements Worker {

    public abstract void doWork(Context context);

    /**
     * 规范了执行顺序
     * @param context
     * @param workerChain
     */
    public final void work(Context context, WorkerChain workerChain){
        doWork(context);
        workerChain.next(context);
    }
}
