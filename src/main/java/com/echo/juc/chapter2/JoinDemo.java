package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.JoinDemo")
public class JoinDemo {
    static int r = 0;
    public static void test() throws InterruptedException {
        log.debug("start");
        Thread t1 = new Thread(() -> {
            log.debug("start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end");
            r = 10;
        });
        t1.start();
        t1.join();  //主线程等待t1线程的结束
        log.debug("result:{}",r);
        log.debug("end");
    }

    public static void main(String[] args) throws InterruptedException {
        test();
    }
}
