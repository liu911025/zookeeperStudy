package com.gupao.zookeeper.javaApiLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class DistributeJavaApiLock {

    private static String ROOT_LOCK = "/lock";
    private String LOCK_ID;
    private static ZooKeeper zooKeeper;
    private static byte[] data = {1,2,3};
    private static int SESSION_TIMEOUT;

    private CountDownLatch countDown = new CountDownLatch(1);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DistributeJavaApiLock() throws IOException, InterruptedException {
        this.zooKeeper = ZookeeperClient.getInstance();
        this.SESSION_TIMEOUT = ZookeeperClient.getSessionTimeout();
    }

    public boolean lock() {
        try {
            LOCK_ID = zooKeeper.create(ROOT_LOCK + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + "创建节点["+ LOCK_ID +"], 开始竞争锁");
            List<String> childrenNodes = zooKeeper.getChildren(ROOT_LOCK, true);
            SortedSet<String> sortedSet = new TreeSet<String>();
            for (String children : childrenNodes) {
                sortedSet.add(ROOT_LOCK + "/" + children);
            }
            String first = sortedSet.first(); //获取最小的锁
            if (LOCK_ID.equals(first)) {
                System.out.println(Thread.currentThread().getName() + "获取到锁,节点["+ LOCK_ID +"]");
                return true;
            }

            SortedSet<String> minNode = sortedSet.headSet(LOCK_ID);
            if (null!= minNode && minNode.size() > 0) {
                String prevLockId = minNode.last();
                zooKeeper.exists(prevLockId, new ZookeeperLockWatcher(countDown));
                countDown.await();
                System.out.println(Thread.currentThread().getName() + "获取锁, 节点["+ LOCK_ID +"]");
            }
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unlock() {
        System.out.println(Thread.currentThread().getName() + "开始释放锁, 节点["+ LOCK_ID +"]");
        try {
            zooKeeper.delete(LOCK_ID, -1);
            System.out.println(Thread.currentThread().getName() + "锁释放成功, 节点["+ LOCK_ID +"]");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            new Thread(() ->{
                DistributeJavaApiLock lock = null;
                try {
                    lock = new DistributeJavaApiLock();
                    latch.countDown();
                    latch.await();
                    lock.lock();
                    TimeUnit.SECONDS.sleep(2);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (null != lock) {
                        lock.unlock();
                    }
                }
            }).start();
        }

    }
}
