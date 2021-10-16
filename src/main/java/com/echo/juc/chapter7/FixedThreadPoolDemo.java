package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "c.FixedThreadPoolDemo")
public class FixedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        ExecutorService customPool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger integer = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread("mypool_t" + integer.getAndIncrement());
            }
        });

        pool.execute(() -> {
            log.debug("1");
        });

        pool.execute(() -> {
            log.debug("2");
        });

        pool.execute(() -> {
            log.debug("3");
        });
    }
}
