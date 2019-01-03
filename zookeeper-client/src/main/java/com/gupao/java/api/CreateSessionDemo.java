package com.gupao.java.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liujiatai on 2019/1/2.
 */
public class CreateSessionDemo {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    private static CountDownLatch countDown = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("Event.KeeperState.SyncConnected: " + Event.KeeperState.SyncConnected) ;
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接状态: " + watchedEvent.getState());
                    countDown.countDown();
                }
            }
        });
        countDown.await();
        System.out.println(zooKeeper.getState());
    }
}
