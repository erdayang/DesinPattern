package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;
import com.mu.yang.filter.Filter;
import com.mu.yang.filter.FilterChain;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFilterChain implements FilterChain {

	private List<Filter> filters = new ArrayList<Filter>();
	
	private int cur = -1;

	public FilterChain addFilter(Filter filter) {
		filters.add(filter);
		return this;
		
	}

	public void doFilter(Context context) {
		cur = cur + 1;
		System.out.println(context.get("yang"));
		if(cur >= filters.size()){
			return;
		}
		
		filters.get(cur).filter(context, this);
		
		//System.out.println(context.get("yang"));
		
	}

	public Context context() {
		// TODO Auto-generated method stub
		return null;
	}

}
