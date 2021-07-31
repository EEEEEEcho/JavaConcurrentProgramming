package com.echo.juc.chapter2;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TimeLimitJoinDemo")
public class TimeLimitJoinDemo {
    static int r1 = 0;
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException{
        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(2);
                r1 = 10;
            }
        };
        long start = System.currentTimeMillis();
        t1.start();
        log.debug("join begin");
        t1.join(1500);  //等待t1 1.5s
        long end = System.currentTimeMillis();
        log.debug("r1 : {}, cost :{}",r1,end - start);
    }
}
