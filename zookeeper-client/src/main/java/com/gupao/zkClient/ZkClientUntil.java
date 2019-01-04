package com.gupao.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by liujiatai on 2019/1/3.
 */
public class ZkClientUntil {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static ZkClient getInstance() {
        ZkClient zkClient = new ZkClient(CONNECTION_STRING, SESSION_TIMEOUT, SESSION_TIMEOUT);
        System.out.println("success...");
        return zkClient;
    }
}
