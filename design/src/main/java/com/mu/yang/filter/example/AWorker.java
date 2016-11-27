package com.mu.yang.filter.example;

import com.mu.yang.filter.AbstractWorker;
import com.mu.yang.filter.Context;

public class AWorker extends AbstractWorker {

	public void doWork(Context context) {
		String name = (String)context.get(Const.KEY);
		name += "xian";
		context.addParam(Const.KEY, name);
	}

}
