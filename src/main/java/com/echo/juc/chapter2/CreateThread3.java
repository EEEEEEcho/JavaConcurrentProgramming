package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j(topic = "c.CreateThread3")
public class CreateThread3 {
    public static void main(String[] args) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("Running");
                Thread.sleep(2000);
                return 10;
            }
        };
        FutureTask<Integer> task = new FutureTask<>(callable);
        Thread thread = new Thread(task,"callable task");
        thread.start();
        try {
            //在主线程中调用FutureTask的get方法，主线程会阻塞 等待线程的执行后返回的结果
            Integer callableReturn = task.get();
            log.debug("{}",callableReturn);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
