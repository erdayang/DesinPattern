package com.mu.yang.rpc.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxianda on 2017/3/4.
 */
public class InstanceMap {

    /**
     * the delegate objects to
     */
    private static final Map<String, Object> objectMap = new HashMap<String, Object>();


    public static void addClass(Class<?> clazz){
        Class<?>[] objs = clazz.getInterfaces();
        try {
            for(Class<?> inter : objs){
                objectMap.put(inter.getName(), clazz.newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getInstance(String key){
        return objectMap.get(key);
    }
}
