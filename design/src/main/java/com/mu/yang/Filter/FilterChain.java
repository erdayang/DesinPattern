package com.mu.yang.Filter;

public interface FilterChain {
	
	public FilterChain addFilter(Filter filter);
	
	public void doFilter(Context context);
	
	public Context context();

}
