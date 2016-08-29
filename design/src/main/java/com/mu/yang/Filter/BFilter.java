package com.mu.yang.Filter;

public class BFilter implements Filter{

	@Override
	public void doFilter(Context context, FilterChain filterChain) {
		String name = (String)context.get("yang");
		
		name += "da";
		
		context.addParam("yang", name);
		filterChain.doFilter(context);
		
	}

}
