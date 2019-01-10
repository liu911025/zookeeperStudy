package com.gupao.zkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class MasterSelector {

    private ZkClient zkClient;

    private final static String MASTER_PATH =  "/master"; //需要争抢的节点

    private IZkDataListener dataListener;   //注册节点内容变化

    private UserCenter server;      //其他服务器

    private UserCenter master;      //master节点

    private static Boolean isRunning = false;

    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);

    public MasterSelector(ZkClient zkClient, UserCenter server) {
        this.zkClient = zkClient;
        this.server = server;
        this.dataListener = new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            public void handleDataDeleted(String dataPath) throws Exception {
                //节点删除,发起master选举操作
                //chooseMaster();
                if (master != null && master.getMc_name().equals(server.getMc_name())) {
                    chooseMaster();
                }else {
                    delayExector.schedule(new Runnable() {
                          @Override
                         public void run() {
                              chooseMaster();
                                                   }
                      },5, TimeUnit.SECONDS);
                }
            }
        };
    }

    public void start() {
        //开始选举
        if (!isRunning) {
            isRunning = true;
            zkClient.subscribeDataChanges(MASTER_PATH, this.dataListener);
            chooseMaster();
        }
    }

    public void stop() {
        //停止
        if (isRunning) {
            isRunning = false;
            zkClient.unsubscribeDataChanges(MASTER_PATH, this.dataListener);
            releaseMaster();
        }
    }

    /**
     * 选举逻辑实现
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private void chooseMaster(){
        if (!isRunning) {
            System.out.println("当前服务未启动!");
            return;
        }
        try {
            zkClient.createEphemeral(MASTER_PATH, server);
            master = server;
            System.out.println("master为: " + master.getMc_name());

            //定时器模拟master故障, 5秒释放一次
            scheduledExecutorService.schedule(()->{
                releaseMaster();
            }, 5, TimeUnit.SECONDS);
        } catch (ZkNodeExistsException e) {
            //创建节点失败,表示节点已存在
            UserCenter userCenter = zkClient.readData(MASTER_PATH, true);
            if (null == userCenter) { //为空再次选举
                chooseMaster();
            }else {
                master = userCenter;
            }
        }
    }

    private void releaseMaster() {
        //释放Master(故障模拟)
        if (checkIsMaster()) {
            zkClient.deleteRecursive(MASTER_PATH);
        }
    }

    /**
     * 判断是否为master
     * @return
     */
    private boolean checkIsMaster() {
        //判断当前是否为Master
        UserCenter userCenter = zkClient.readData(MASTER_PATH);
        if (userCenter.getMc_name().equals(server.getMc_name())) {
            return true;
        }
        return false;
    }
}


