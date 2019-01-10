package com.gupao.zkClient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujiatai on 2019/1/4.
 */
public class MasterSelectorTest {

    private static final String CONNECTION_STRING = "192.168.25.133:2181,192.168.25.134:2181,192.168.25.135:2181";
    private static final int SESSION_TIMEOUT = 5000;

    //启动的服务个数
    private static final int CLIENT_QTY = 10;
    public static void main(String[] args) throws Exception {
        //保存所有zkClient的列表
        List<ZkClient> clients = new ArrayList<>();
        //保存所有服务的列表
        List<MasterSelector> workServers = new ArrayList<>();

        try {
            for (int i = 0; i < CLIENT_QTY; ++i) {
                //创建zkClient
                ZkClient client = new ZkClient(CONNECTION_STRING, 5000, 5000, new SerializableSerializer());
                clients.add(client);
                //创建serverData
                UserCenter runningData = new UserCenter();
                runningData.setMc_id(i);
                runningData.setMc_name("Client #" + i);
                //创建服务
                MasterSelector workServer = new MasterSelector(client, runningData);

                workServers.add(workServer);
                workServer.start();
            }

            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            System.out.println("Shutting down...");

            for (MasterSelector workServer : workServers) {
                try {
                    workServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (ZkClient client : clients) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
