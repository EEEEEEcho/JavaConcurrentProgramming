package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j(topic = "c.ScheduledThreadPoolDemo")
public class ScheduledThreadPoolDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //delayTask();
        log.debug("start....");
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.scheduleAtFixedRate(() -> {
            log.debug("running....");
            try{
                int i = 1 / 0;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1,2,TimeUnit.SECONDS);
        //创建一个定时任务，延时1秒之后开始执行，每隔2秒执行一次。


//        pool.scheduleWithFixedDelay(()->{
//            log.debug("running....");
//
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },1,2,TimeUnit.SECONDS);
        //创建一个定时任务，在延迟1秒之后执行，执行任务时，在每次任务执行完毕后，延迟2秒，再执行下一次任务

        ExecutorService pool1 = Executors.newFixedThreadPool(1);
        Future<Boolean> future = pool1.submit(() -> {
            log.debug("Hello");
            int i = 1 / 0;
            return true;
        });
        future.get();

    }

    private static void delayTask() {
        //创建一个容量为2的线程池
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        //延时一秒之后执行任务1，任务1需要花费2秒去执行。
        pool.schedule(() -> {
            log.debug("task1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1, TimeUnit.SECONDS);

        //同样延时1秒之后执行任务2
        pool.schedule(() -> {
            log.debug("task2");
        },1,TimeUnit.SECONDS);
    }
}
