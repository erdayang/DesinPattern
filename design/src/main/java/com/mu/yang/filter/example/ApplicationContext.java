package com.mu.yang.filter.example;

import com.mu.yang.filter.Context;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext implements Context {

	
	private Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public void addParam(String key, Object obj) {
		map.put(key, obj);
		
	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return map.get(key);
	}

}
