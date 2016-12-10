package com.mu.yang.worker.example;

import com.mu.yang.worker.Context;
import com.mu.yang.worker.WorkerChain;

public class Application {

	public static void main(String [] args){
		Context context = new ApplicationContext();
		context.addParam(Const.KEY, "");
		WorkerChain chain = new ApplicationWorkerChain();
		chain.addWorker(new AWorker())
				.addWorker(new BWorker());
		chain.next(context);
		System.out.println(context.get(Const.KEY));
	}
}
