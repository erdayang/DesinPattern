package com.mu.yang.worker.example;

import com.mu.yang.worker.AbstractWorker;
import com.mu.yang.worker.Context;

public class AWorker extends AbstractWorker {

	public void doWork(Context context) {
		String name = (String)context.getParam(Const.KEY);
		name += "xian";
		context.addParam(Const.KEY, name);
	}

}
