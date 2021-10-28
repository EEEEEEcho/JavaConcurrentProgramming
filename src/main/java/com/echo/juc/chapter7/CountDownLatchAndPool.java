package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.CountDownLatchAndPool")
public class CountDownLatchAndPool {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(3);
        pool.submit(()->{
            log.debug("t1 begin....");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1 end....");
            //线程1结束后，计数器减一下
            latch.countDown();
        });

        pool.submit(()->{
            log.debug("t2 begin....");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2 end....");
            //线程2结束后，计数器减一下
            latch.countDown();
        });

        pool.submit(()->{
            log.debug("t3 begin....");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t3 end....");
            //线程1结束后，计数器减一下
            latch.countDown();
        });
        //最后一个线程等待其余三个线程结束
        pool.submit(() -> {
            try {
                log.debug("waiting...");
                latch.await();
                log.debug("wait end...");
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        });
    }
}
