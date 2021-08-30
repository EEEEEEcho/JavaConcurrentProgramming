package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.InterruptDemo")
public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("sleep...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        log.debug("interrupt");
        //等t1睡着
        TimeUnit.SECONDS.sleep(1);
        //打断
        t1.interrupt();
        log.debug("interrupt flg {}",t1.isInterrupted());
    }
}
