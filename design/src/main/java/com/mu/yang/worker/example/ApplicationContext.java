package com.mu.yang.worker.example;

import com.mu.yang.worker.Context;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext implements Context {

	
	private Map<String, Object> map = new HashMap<String, Object>();

	public void addParam(String key, Object obj) {
		map.put(key, obj);
		
	}

	public Object getParam(String key) {
		return map.get(key);
	}

}
