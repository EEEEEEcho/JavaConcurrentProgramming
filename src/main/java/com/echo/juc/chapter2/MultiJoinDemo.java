package com.echo.juc.chapter2;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j(topic = "c.MultiJoinDemo")
public class MultiJoinDemo {
    static int r1 = 0;
    static int r2 = 0;

    private static void test2() throws InterruptedException{
        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(1);
                r1 = 10;
            }
        };
        Thread t2 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(2);
                r2 = 20;
            }
        };
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        log.debug("join begin");
        t2.join();
        log.debug("t2 join end");
        t1.join();
        log.debug("t1 join end");
        long end = System.currentTimeMillis();
        log.debug("r1 : {} , r2 : {} , cost : {}",r1,r2,end - start);
    }

    public static void main(String[] args) throws InterruptedException {
        test2();
    }
}
