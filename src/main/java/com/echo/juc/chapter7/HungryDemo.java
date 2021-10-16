package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j(topic = "c.HungryDemo")
public class HungryDemo {
    static final List<String> MENU = Arrays.asList("地三鲜","宫保鸡丁","辣子鸡","炖大鹅");
    static Random random = new Random();
    static String cooking(){
        return MENU.get(random.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        //服务员线程池，专门处理点餐任务。
        ExecutorService waiterPool = Executors.newFixedThreadPool(2);

        //厨师线程池，专门处理做饭任务
        ExecutorService cookerPool = Executors.newFixedThreadPool(2);


        //提交一个点餐的任务
        waiterPool.execute(() -> {
            log.debug("处理点餐");
            //在点餐的任务中再提交一个做菜的任务给厨师，并等待菜品做出
            Future<String> result = cookerPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜：{}",result.get());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

        //再提交一个点餐的任务
        waiterPool.execute(() -> {
            log.debug("处理点餐");
            //在点餐的任务中再提交一个做菜的任务，并等待菜品做出
            Future<String> result = cookerPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜：{}",result.get());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
