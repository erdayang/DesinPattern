package com.mu.yang.filter.example;

import com.mu.yang.filter.AbstractWorker;
import com.mu.yang.filter.Context;

public class AWorker extends AbstractWorker {

	public void doFilter(Context context) {
		String name = (String)context.get("yang");
		
		name += "xian";
		
		context.addParam("yang", name);
	}

}
