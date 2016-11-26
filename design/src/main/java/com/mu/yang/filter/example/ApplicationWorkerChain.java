package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;
import com.mu.yang.filter.Worker;
import com.mu.yang.filter.WorkerChain;

import java.util.ArrayList;
import java.util.List;

public class ApplicationWorkerChain implements WorkerChain {

	private List<Worker> workers = new ArrayList<Worker>();
	
	private int cur = -1;

	public WorkerChain addFilter(Worker worker) {
		workers.add(worker);
		return this;
		
	}

	public void doFilter(Context context) {
		cur = cur + 1;
		System.out.println(context.get("yang"));
		if(cur >= workers.size()){
			return;
		}
		
		workers.get(cur).work(context, this);
		
		//System.out.println(context.get("yang"));
		
	}

	public Context context() {
		// TODO Auto-generated method stub
		return null;
	}

}
