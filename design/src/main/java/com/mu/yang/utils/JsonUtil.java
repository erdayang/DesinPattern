package com.mu.yang.utils;

import com.google.gson.Gson;

/**
 * Created by xuanda007 on 2016/11/26.
 */
public class JsonUtil {
    public static Gson gson = new Gson();

    public static String getJson(Object o){
        return gson.toJson(o);
    }

}
