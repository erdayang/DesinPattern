package com.mu.yang.proxy;

/**
 * Created by yangxianda on 2016/12/10.
 */
public class HelloWorld implements IHelloWorld {
    public String get(String name) {
        return "this is an proxy test";
    }

    public String get() {
        return null;
    }
}
