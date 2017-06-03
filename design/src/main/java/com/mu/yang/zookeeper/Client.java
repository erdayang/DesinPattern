package com.mu.yang.zookeeper;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.util.List;

/**
 * Created by xuanda007 on 2017/3/9.
 */
public class Client {

    private static final String ZK_ADDRESS = "127.0.0.1";

    public static void main(String [] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000));
        client.start();
        List<String> list = client.getChildren().forPath("/");
        for(String str: list){
            System.out.println(str);
        }

    }
}
