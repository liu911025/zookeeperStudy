package com.gupao.zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujiatai on 2019/1/3.
 */
public class ZkClientApiDemo {

    private static String path = "/zkClient";
    private static CountDownLatch countDown = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = ZkClientUntil.getInstance();

        //创建节点
        //String s = zkClient.create(path, path, CreateMode.PERSISTENT);
        // zkClient.createPersistent("/eee");

        //zkClient.writeData(path, "123".getBytes()); //修改节点数据

        //创建多级节点, true表示创建多级节点
        //zkClient.createPersistent("/eee/rrr", true);

        //删除节点
       // boolean delete = zkClient.delete(path);

        //递归删除节点
        /*boolean delete = zkClient.deleteRecursive("/eee");
        System.out.println(delete);*/

        //获取子节点
        /*List<String> children = zkClient.getChildren("/eee");
        for (String child: children) {
            System.out.println(child);
        }*/

        //节点数据变更, 监听
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("节点数据变更: " + dataPath + "-->" + data);
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("节点数据删除: " + dataPath);
            }
        });

        //zkClient.writeData(path, "www"); //修改节点数据
        zkClient.delete(path);
        TimeUnit.SECONDS.sleep(10);

       /* zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

            }
        });*/
    }
}
