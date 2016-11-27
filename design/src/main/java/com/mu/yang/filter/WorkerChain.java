package com.mu.yang.filter;

public interface WorkerChain {
	
	public WorkerChain addWorker(Worker worker);
	
	public void start(Context context);
	
	public Context context();

}
