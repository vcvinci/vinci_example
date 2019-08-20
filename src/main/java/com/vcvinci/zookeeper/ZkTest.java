package com.vcvinci.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author vinci
 * @Title: ZkTest
 * @ProjectName Leecode
 * @Description: TODO
 * @date 2019-06-0615:30
 */
public class ZkTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        String CONNECT_ADDR = "127.0.0.1:2181";
        ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), 10000);
        //zkc.createPersistent("/vinci/test", true);
      /*  HashMap<String, Object> test = new HashMap<>(4);
        test.put("0-m", 4);
        ObjectMapper mapper = new ObjectMapper();
        zkc.writeData("/vinci/test", mapper.writeValueAsString(test));*/
        /*Thread t1 = new Thread(() -> {
            ObjectMapper mapper = new ObjectMapper();
            String strFromZk = zkc.readData("/vinci/test").toString();
            System.out.println("t1:" + strFromZk);
            Map<String, Object> map = null;
            try {
                map = mapper.readValue(strFromZk, new TypeReference<Map<String, Object>>() {
                });
                map.put("1-m", 4);
                zkc.writeData("/vinci/test", mapper.writeValueAsString(map));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t1---======");

        Thread t2 = new Thread(() -> {
            ObjectMapper mapper = new ObjectMapper();
            String strFromZk = zkc.readData("/vinci/test").toString();
            System.out.println("t2:" + strFromZk);
            try {
                Map<String, Object> map = mapper.readValue(strFromZk, new TypeReference<Map<String, Object>>() {
                });
                map.put("2-m", 5);
                zkc.writeData("/vinci/test", mapper.writeValueAsString(map));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2---======");
        t1.start();
        t2.start();*/

        TimeUnit.SECONDS.sleep(1);
        Map<String, Object> map = zkc.readData("/vinci/test");
        System.out.println("result:" + map.toString());

    }
}
