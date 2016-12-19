package com.mu.yang.rpc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xuanda007 on 2016/12/19.
 */
public class NetUtils {

    public static InetAddress getInetAddress(String ip){
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }
}
