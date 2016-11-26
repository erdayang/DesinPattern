package com.mu.yang.filter;

public interface WorkerChain {
	
	public WorkerChain addFilter(Worker worker);
	
	public void doFilter(Context context);
	
	public Context context();

}
