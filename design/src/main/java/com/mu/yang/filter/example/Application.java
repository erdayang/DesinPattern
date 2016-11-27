package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;
import com.mu.yang.filter.WorkerChain;

public class Application {

	public static void main(String [] args){
		Context context = new ApplicationContext();
		context.addParam(Const.KEY, "");
		WorkerChain chain = new ApplicationWorkerChain();
		chain.addWorker(new AWorker()).addWorker(new BWorker());
		chain.start(context);
		System.out.println(context.get(Const.KEY));
	}
}
