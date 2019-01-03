package com.gupao.java.api;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liujiatai on 2019/1/3.
 */
public class CreateAuthDemo {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    private static String path = "/auth";

    private static CountDownLatch countDown = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

       //创建节点
      /* ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new ApiWatcherImpl(countDown));

        //添加digest权限,账号密码为: root:root
        zooKeeper.addAuthInfo("digest", "root:root".getBytes());

        zooKeeper.exists(path, true);
        String auth = zooKeeper.create(path, "auth".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);*/


       ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new ApiWatcherImpl(countDown));
       zooKeeper.addAuthInfo("digest", "root:root".getBytes());
       Stat stat = zooKeeper.setData(path, (path + "123").getBytes(), -1);

    }
}
