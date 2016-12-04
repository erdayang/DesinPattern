package com.mu.yang.worker;

public interface WorkerChain {
	
	public WorkerChain addWorker(Worker worker);
	
	public void start(Context context);
	
	public Context context();

}
