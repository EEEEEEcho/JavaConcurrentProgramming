package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j(topic = "c.SubmitDemo")
public class SubmitDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        testSubmit();
        testInvokeAny();
    }

    private static void testSubmit() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //提交任务并拿到future对象
        Future<String> result = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                return "OK";
            }
        });
        //拿到线程的执行结果
        log.debug("future get {}",result.get());
    }

    private static void testInvokeAll() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Future<Object>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(2);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(3);
                    return "3";
                }));

        futures.forEach(f -> {
            try{
                log.debug("{}",f.get());
            }
            catch (InterruptedException |ExecutionException e){
                e.printStackTrace();
            }
        });

    }

    private static void testInvokeAny() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //invokeAny,提交一个包含所有任务的集合，集合中哪个任务最先执行完毕，就将执行完毕的结果返回，
        //并停止集合中所有其他线程的执行
        String result = pool.invokeAny(Arrays.asList(
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(2);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(3);
                    return "3";
                }
        ));
        log.debug("{}",result);
    }
}
