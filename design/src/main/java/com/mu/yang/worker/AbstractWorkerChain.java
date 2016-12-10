package com.mu.yang.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yangxianda on 2016/12/4.
 */
public class AbstractWorkerChain implements WorkerChain{
    private List<Worker> workers;
    private int cur = -1;

    public AbstractWorkerChain(){
        workers = new ArrayList<Worker>();
    }

    public AbstractWorkerChain(Collection<? extends Worker> collection){
        workers.addAll(collection);
    }

    public WorkerChain addWorker(Worker worker) {
        workers.add(worker);
        return this;
    }

    /**
     * 不允许更给的执行步骤
     * @param context
     */
    public final void next(Context context) {
        cur = cur + 1;
        if(cur >= workers.size()){
            return;
        }
        before(context);
        workers.get(cur).work(context, this);
        after(context);
    }

    public void before(Context context){
    }
    public void after(Context context){
    }

}
