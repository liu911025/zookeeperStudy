package com.gupao.java.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liujiatai on 2019/1/2.
 */
public class JavaApiDemo {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    private static String path = "/ljt";

    private static CountDownLatch countDown = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        final ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
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
        });
        countDown.await();

       /* //创建
        Stat ljtNode = zooKeeper.exists(path, true);//注册watch
        if (null == ljtNode) {
            String s = zooKeeper.create(path, "ljt".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(s);
        }

        //修改
        zooKeeper.exists(path, true);  //注册watch
        Stat stat = zooKeeper.setData(path, "ljt123".getBytes(), -1);
        System.out.println(stat.getDataLength());

        String childPath = path + "/ljt-1";
        //创建子节点
        Stat stat2 = zooKeeper.exists(childPath, true);//判断节点是否存在, true代表注册watch
        if (null == stat2) {
            String s1 = zooKeeper.create(childPath, (path + "/ljt-1").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(s1);

            //修改子节点
            zooKeeper.exists(childPath, true);  //注册watchzooKeeper.exists(path + "/ljt-1", true);  //注册watch
            Stat stat1 = zooKeeper.setData(childPath, "ljt123".getBytes(), -1);
            System.out.println(stat1.getDataLength());
        }*/

      /*  //删除节点
        zooKeeper.exists(path, true);
        zooKeeper.delete(path, -1);*/

        List<String> children = zooKeeper.getChildren(path, true);
        System.out.println(children);
    }
}
