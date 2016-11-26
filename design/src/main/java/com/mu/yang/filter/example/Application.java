package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;
import com.mu.yang.filter.FilterChain;

public class Application {

	public static void main(String [] args){
		Context context = new ApplicationContext();
		context.addParam("yang", "");
		
		FilterChain chain = new ApplicationFilterChain();
		chain.addFilter(new AFilter()).addFilter(new BFilter());
		chain.doFilter(context);
	}
}
