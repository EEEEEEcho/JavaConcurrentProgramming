package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j(topic = "c.SingleThreadExecutorDemo")
public class SingleThreadExecutorDemo {
    public static void main(String[] args) {
        test();
    }
    public static void test(){
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(() -> {
            log.debug("1");
            int i = 1 / 0;  //抛出异常
        });

        pool.execute(() -> {
            log.debug("2");
        });

        pool.execute(() -> {
            log.debug("3");
        });
    }
}
