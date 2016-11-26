package com.mu.yang.filter.example;

import com.mu.yang.filter.AbstractFilter;
import com.mu.yang.filter.Context;
import com.mu.yang.filter.Filter;
import com.mu.yang.filter.FilterChain;

public class AFilter extends AbstractFilter {

	public void doFilter(Context context) {
		String name = (String)context.get("yang");
		
		name += "xian";
		
		context.addParam("yang", name);
	}

}
