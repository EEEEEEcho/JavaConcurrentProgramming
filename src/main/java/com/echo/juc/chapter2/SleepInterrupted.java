package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.SleepInterrupted")
public class SleepInterrupted {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep....");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.debug("wake up ...");
                    e.printStackTrace();
                }
            }
        };
        t1.start();

        //主线程睡一秒
        Thread.sleep(1000);
        //唤醒t1
        log.debug("interrupt ...");
        t1.interrupt();
    }
}
