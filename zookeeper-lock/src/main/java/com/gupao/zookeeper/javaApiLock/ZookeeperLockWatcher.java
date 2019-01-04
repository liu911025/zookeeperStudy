package com.gupao.zookeeper.javaApiLock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class ZookeeperLockWatcher implements Watcher {

    private CountDownLatch countDown;

    public ZookeeperLockWatcher(CountDownLatch countDown) {
        this.countDown = countDown;
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            countDown.countDown();
        }
    }
}
