package com.gupao.java.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * Created by liujiatai on 2019/1/3.
 */
public class ApiWatcherImpl implements Watcher {

    private CountDownLatch countDown;

    public ApiWatcherImpl(CountDownLatch countDown) {
        this.countDown = countDown;
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("连接状态: " + watchedEvent.getState());
            countDown.countDown();
            System.out.println("创建类型: " + watchedEvent.getType());
            if (Event.EventType.NodeCreated == watchedEvent.getType()) {
                System.out.println("创建节点: " + watchedEvent.getPath() + ",创建类型: " + watchedEvent.getType());
            }else if (Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                System.out.println("节点数据变更: " + watchedEvent.getPath() + ",类型: " + watchedEvent.getType());
            }else if (Event.EventType.NodeChildrenChanged == watchedEvent.getType()) {
                System.out.println("子节点数据变更: " + watchedEvent.getPath() + ",类型: " + watchedEvent.getType());
            }else if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
                System.out.println("节点删除: " + watchedEvent.getPath() + ",类型: " + watchedEvent.getType());
            }
        }
    }
}
