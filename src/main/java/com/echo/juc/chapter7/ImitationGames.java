package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j(topic = "c.ImitationGames")
public class ImitationGames {
    public static void main(String[] args) throws Exception {
        //10个线程，代表10个玩家
        ExecutorService pool = Executors.newFixedThreadPool(10);
        //数组中的每个元素代表每个线程的加载进度
        String[] process = new String[10];
        //随机睡眠的时间
        Random random = new Random();
        //计数器锁
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int k = i;  // lambda表达式中，不能引用外部的变量，所以需要定义一个常量在这里，
            pool.execute(()->{
                for (int j = 0; j <= 100; j++) {
                    process[k] = j + "%";
                    try {
                        //模拟加载延迟
                        TimeUnit.MILLISECONDS.sleep(random.nextInt(100));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    //将打印改为print并在前面加上\r会使后面的结果覆盖前面的结果打印。
                    System.out.print("\r" + Arrays.toString(process));
                }
                //计数完成，计数器锁减一
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("\n欢迎来到英雄联盟");
        pool.shutdown();
    }
//    public void test(){
//        ResTemplate template = new ResTemplate();
//        log.debug("begin");
//        //取得订单信息
//        Map<String,Object> order = template.getForObject("http://localhost:8080/order/{1}",Map.class,1);
//        log.debug("order is : {}",order);
//        //取得商品信息
//        Map<String,Object> product1 = template.getForObject("http://localhost:8080/product/{1}",Map.class,1);
//        Map<String,Object> product2 = template.getForObject("http://localhost:8080/product/{2}",Map.class,2);
//        log.debug("product is : {},{}",product1,product2);
//        //快递信息
//        Map<String,Object> express = template.getForObject("http://localhost:8080/express/{1}",Map.class,1);
//        log.debug("express id : {}",express);
//
//
//
//    }
//    public void test2(){
//        ResTemplate template = new ResTemplate();
//        log.debug("begin");
//        ExecutorService pool = Executors.newCachedThreadPool();
//        Future<Map<String,Object>> order = pool.submit(() -> {
//            //取得订单信息
//            return template.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
//        });
//        Future<Map<String,Object>> product1 = pool.submit(() -> {
//            //取得商品信息
//            return template.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
//        });
//
//        Future<Map<String,Object>> product2 = pool.submit(() -> {
//            return template.getForObject("http://localhost:8080/product/{2}", Map.class, 2);
//        });
//        Future<Map<String,Object>> express = pool.submit(() -> {
//            //快递信息
//            return template.getForObject("http://localhost:8080/express/{1}", Map.class, 1);
//        });
//        System.out.println(order.get());
//        System.out.println(product1.get());
//        System.out.println(product2.get());
//        System.out.println(express.get());
//        //do other logic
//    }
}
