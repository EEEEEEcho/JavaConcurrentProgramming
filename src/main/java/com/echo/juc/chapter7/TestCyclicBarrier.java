package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j(topic = "c.TestCyclicBarrier")
public class TestCyclicBarrier {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建一个计数为2，并包含一个结束任务的barrier，当计数到达0之后，会执行定义的结束任务
        //如果计数器变为了0，再调用await()方法，计数器会重新从2开始计数。
        CyclicBarrier barrier = new CyclicBarrier(2,()->{
            log.debug("task1 task2 finish...");
        });
        for (int i = 0; i < 3; i++) {
            pool.submit(()->{
                log.debug("task1 begin....");
                try {
                    TimeUnit.SECONDS.sleep(1);
                    //类似于latch.countDown() 2 - 1 = 1 ，调用这个方法，会让线程阻塞在这里，
                    //等待这个计数器减为0之后，才继续运行
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });

            pool.submit(() -> {
                log.debug("task2 begin....");
                try {
                    TimeUnit.SECONDS.sleep(2);
                    barrier.await();    //类似于latch.countDown() 1-1=0
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }
}
