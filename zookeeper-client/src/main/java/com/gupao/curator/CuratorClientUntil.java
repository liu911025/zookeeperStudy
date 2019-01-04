package com.gupao.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class CuratorClientUntil {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static CuratorFramework getInstance() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTION_STRING).connectionTimeoutMs(SESSION_TIMEOUT).sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        curatorFramework.start();
        return curatorFramework;
    }
}
