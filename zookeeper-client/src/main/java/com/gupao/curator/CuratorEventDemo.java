package com.gupao.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

/**
 * Created by liujiatai on 2019/1/4.
 *
 * 节点监听事件
 */
public class CuratorEventDemo {

    /**
     *  三种watcher节点监听
     *  pathCache 监听一个路径下子节点的创建
     *  nodeCache 监听一个节点的创建, 更新, 删除
     *  treeCache pathCache + nodeCache的结合体 (监听路径下的创建, 更新, 删除)
     *  缓存路径下的所有节点数据
     */

    private static String path = "/ljt";

    public static void main(String[] args) throws Exception {
        CuratorFramework connect = CuratorClientUntil.getInstance();

        /**
         * nodeCache
         */
       /* NodeCache cache = new NodeCache(connect, path);
        cache.start();

        cache.getListenable().addListener(() -> System.out.println("节点数据变更,变更结果为:" + new String(cache.getCurrentData().getData())));

        Stat stat = connect.setData().forPath(path, "eee".getBytes());
        System.out.println(stat);*/

        /**
         * pathCache
         */
        //true 为是否缓存
        PathChildrenCache cache = new PathChildrenCache(connect, "/qqq", true);
        /*
            PathChildrenCache.StartMode.NORMAL                  //初始化为空,不缓存
            PathChildrenCache.StartMode.BUILD_INITIAL_CACHE     //结果返回reBuild
            PathChildrenCache.StartMode.POST_INITIALIZED_EVENT
         */
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch(pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED :
                        System.out.println("创建子节点," + pathChildrenCacheEvent.getData().getPath());
                        break;
                    case CHILD_UPDATED :
                        System.out.println("更新子节点," + pathChildrenCacheEvent.getData().getPath());
                        break;
                    case CHILD_REMOVED :
                        System.out.println("删除子节点," + pathChildrenCacheEvent.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        });
        String path = connect.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/qqq/qqq1/qqq1-1", "qqq".getBytes());
        System.out.println(path);
        TimeUnit.SECONDS.sleep(2);

        connect.setData().forPath("/qqq/qqq1", "wwww".getBytes());
        TimeUnit.SECONDS.sleep(2);

        connect.delete().deletingChildrenIfNeeded().forPath("/qqq/qqq1");
        TimeUnit.SECONDS.sleep(2);

        System.in.read();
    }
}
