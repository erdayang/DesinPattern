package com.mu.yang.filter;

public interface Context {

	
	public void addParam(String key, Object obj);
	
	public Object get(String key);
}
