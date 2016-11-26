package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;
import com.mu.yang.filter.Filter;
import com.mu.yang.filter.FilterChain;

public class BFilter implements Filter {

	@Override
	public void filter(Context context, FilterChain filterChain) {
		String name = (String)context.get("yang");
		
		name += "da";
		
		context.addParam("yang", name);
		filterChain.doFilter(context);
		
	}

}
