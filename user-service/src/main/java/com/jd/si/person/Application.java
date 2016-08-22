package com.jd.si.person;

import com.jd.si.person.service.UserServiceImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;

/**
 * Created by yangxianda on 2016/8/22.
 */
public class Application {


    public static void main(String [] args) throws TTransportException {
        TServerTransport serverTransport = new TServerSocket(9090);
        TServer server = new TSimpleServer(
                new THsHaServer
                        .Args((TNonblockingServerTransport) serverTransport)
                            .processor(new UserServiceImpl()));

        // Use this for a multithreaded server
        // TServer server = new TThreadPoolServer(new
        // TThreadPoolServer.Args(serverTransport).processor(processor));

        System.out.println("Starting the simple server...");
        server.serve();
    }
}
