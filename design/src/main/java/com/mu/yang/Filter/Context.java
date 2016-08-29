package com.mu.yang.Filter;

public interface Context {

	
	public void addParam(String key, Object obj);
	
	public Object get(String key);
}
