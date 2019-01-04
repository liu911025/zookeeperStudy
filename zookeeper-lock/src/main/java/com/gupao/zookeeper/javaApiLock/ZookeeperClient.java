package com.gupao.zookeeper.javaApiLock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class ZookeeperClient {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static CountDownLatch countDown = new CountDownLatch(1);

    public static ZooKeeper getInstance() throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDown.countDown();
                }
            }
        });
        countDown.await();
        return zooKeeper;
    }

    public static int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }
}
