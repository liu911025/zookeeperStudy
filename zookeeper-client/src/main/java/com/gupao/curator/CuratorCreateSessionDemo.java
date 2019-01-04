package com.gupao.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class CuratorCreateSessionDemo {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) {

        /**
         * curator连接的重试策略
         * ExponentialBackoffRetry()  衰减重试
         * RetryNTimes 指定最大重试次数
         * RetryOneTime 仅重试一次
         * RetryUnitilElapsed 一直重试知道规定的时间
         */
        //第一种连接方式
        /*CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTION_STRING,
                SESSION_TIMEOUT, SESSION_TIMEOUT, new ExponentialBackoffRetry(1000, 3));
        curatorFramework.start(); //启动连接*/

        //第二种 fluent
        /*CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTION_STRING).connectionTimeoutMs(SESSION_TIMEOUT).sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        curatorFramework.start();*/

        /**
         * namespace("/ljt")  表示所有操作都是在/ljt节点下
         */
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTION_STRING).connectionTimeoutMs(SESSION_TIMEOUT).sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("/ljt").build();
        curatorFramework.start();
        System.out.println("success...");
    }
}
