package com.mu.yang.worker;

public interface Context {

	
	public void addParam(String key, Object obj);
	
	public Object getParam(String key);
}
