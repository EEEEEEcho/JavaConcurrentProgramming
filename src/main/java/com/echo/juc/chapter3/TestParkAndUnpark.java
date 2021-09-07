package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.TestParkAndUnpark")
public class TestParkAndUnpark {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                log.debug("start...");
                //t1线程4秒之后才会执行park
                TimeUnit.SECONDS.sleep(4);
                log.debug("park...");
                LockSupport.park();
                log.debug("resume...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        //然而主线程在2秒之后就执行了unpark
        TimeUnit.SECONDS.sleep(2);
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
