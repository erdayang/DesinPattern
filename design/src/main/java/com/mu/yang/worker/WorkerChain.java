package com.mu.yang.worker;

public interface WorkerChain {
	
	public WorkerChain addWorker(Worker worker);
	
	public void next(Context context);
	
}
