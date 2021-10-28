package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestCountDownLatch")
public class TestCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(()->{
            log.debug("t1 begin....");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1 end....");
            //线程1结束后，计数器减一下
            latch.countDown();
        }).start();

        new Thread(()->{
            log.debug("t2 begin....");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2 end....");
            //线程2结束后，计数器减一下
            latch.countDown();
        }).start();

        new Thread(()->{
            log.debug("t3 begin....");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t3 end....");
            //线程1结束后，计数器减一下
            latch.countDown();
        }).start();
        latch.await();

        log.debug("三个线程全部 执行完毕。。。");
    }
}
