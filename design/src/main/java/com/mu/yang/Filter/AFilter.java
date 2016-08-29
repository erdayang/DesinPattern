package com.mu.yang.Filter;

public class AFilter implements Filter{

	public void doFilter(Context context, FilterChain filterChain) {
		String name = (String)context.get("yang");
		
		name += "xian";
		
		context.addParam("yang", name);
		filterChain.doFilter(context);
	}

}
