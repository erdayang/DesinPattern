package com.mu.yang.Filter;

public class Application {

	public static void main(String [] args){
		Context context = new ApplicationContext();
		context.addParam("yang", "");
		
		FilterChain chain = new ApplicationFilterChain();
		chain.addFilter(new AFilter()).addFilter(new BFilter());
		chain.doFilter(context);
	}
}
