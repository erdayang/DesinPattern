package com.mu.yang.Filter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFilterChain implements FilterChain{

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
		
		filters.get(cur).doFilter(context, this);
		
		//System.out.println(context.get("yang"));
		
	}

	public Context context() {
		// TODO Auto-generated method stub
		return null;
	}

}
