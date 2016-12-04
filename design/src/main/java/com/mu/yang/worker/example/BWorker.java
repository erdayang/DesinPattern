package com.mu.yang.worker.example;


import com.mu.yang.worker.AbstractWorker;
import com.mu.yang.worker.Context;

public class BWorker extends AbstractWorker {

	public void doWork(Context context) {
		String name = (String)context.get(Const.KEY);

		name += "da";

		context.addParam(Const.KEY, name);
	}

}
