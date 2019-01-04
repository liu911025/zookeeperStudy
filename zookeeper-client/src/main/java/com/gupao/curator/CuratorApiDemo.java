package com.gupao.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import javax.sql.CommonDataSource;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class CuratorApiDemo {

    private static CountDownLatch countDown = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        CuratorFramework connect = CuratorClientUntil.getInstance();

        /**
         * 创建节点
         */
        //connect.createContainers("/cu");
        //创建多级节点
        /*String path = connect.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/curator/curator1", "123".getBytes());
        System.out.println(path);
        */

        /**
         * 异步创建节点
         */
        /*ExecutorService executorService = Executors.newFixedThreadPool(1);


        connect.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println(curatorEvent.getPath() + "->" + curatorEvent.getName() + "->" + new String(curatorEvent.getData()));
                countDown.countDown();
            }
        }, executorService).forPath("/lyx", "lyx".getBytes());
        countDown.await();
        executorService.shutdown();*/
        /**
         * 删除节点
         */
        //connect.delete().forPath("/cu");
        //删除多级节点,如果存在子节点  默认verson为-1, 最新版本
        //connect.delete().deletingChildrenIfNeeded().forPath("/curator");

        /**
         * 获取数据
         * storingStatIn(stat) 将节点信息封装到stat中
         */
        /*Stat stat = new Stat();
        byte[] bytes = connect.getData().storingStatIn(stat).forPath("/curator");
        System.out.println(new String(bytes) + "--> stat: " + stat);*/

        /**
         * 修改数据
         */
        /*Stat stat = connect.setData().forPath("/curator", "ljt".getBytes());
        System.out.println(stat);*/

        /**
         * 事务操作 (curator独有)
         */
        Collection<CuratorTransactionResult> results = connect.inTransaction().create().withMode(CreateMode.PERSISTENT).forPath("/qqq", "qqq".getBytes()).and()
                .setData().forPath("/lyx", "www".getBytes()).and().commit();
        for (CuratorTransactionResult c : results) {
            System.out.println(c.getForPath() + "-->" + c.getResultPath());
        }
    }
}
