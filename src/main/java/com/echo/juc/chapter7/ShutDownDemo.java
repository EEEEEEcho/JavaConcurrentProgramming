package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j(topic = "c.ShutDownDemo")
public class ShutDownDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> res1 = pool.submit(() -> {
            log.debug("task1 running ...");
            Thread.sleep(1000);
            log.debug("task1 finish ...");
            return 1;
        });

        Future<Integer> res2 = pool.submit(() -> {
            log.debug("task2 running ...");
            Thread.sleep(2000);
            log.debug("task2 finish ...");
            return 2;
        });

        Future<Integer> res3 = pool.submit(() -> {
            log.debug("task3 running ...");
            Thread.sleep(3000);
            log.debug("task3 finish ...");
            return 3;
        });


        log.debug("shut down");
        //pool.shutdown();

        List<Runnable> runnables = pool.shutdownNow();
        log.debug("others : {}",runnables);
    }
}
