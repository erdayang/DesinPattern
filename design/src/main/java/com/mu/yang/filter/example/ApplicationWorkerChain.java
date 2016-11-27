package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;
import com.mu.yang.filter.Worker;
import com.mu.yang.filter.WorkerChain;

import java.util.ArrayList;
import java.util.List;

public class ApplicationWorkerChain implements WorkerChain {

	private List<Worker> workers = new ArrayList<Worker>();
	
	private int cur = -1;

	public WorkerChain addWorker(Worker worker) {
		workers.add(worker);
		return this;
		
	}

	@Override
	public void start(Context context) {
		cur = cur + 1;
		if(cur >= workers.size()){
			return;
		}
		workers.get(cur).work(context, this);
	}

	public Context context() {
		// TODO Auto-generated method stub
		return null;
	}

}
