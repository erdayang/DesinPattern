package com.mu.yang.rpc.server;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * Created by yangxianda on 2017/3/6.
 */
public class Writer extends Thread {
    private final Selector writeSelector;
    public Writer(String name) throws IOException {
        this.setName(name);
        writeSelector = Selector.open();
    }
}
