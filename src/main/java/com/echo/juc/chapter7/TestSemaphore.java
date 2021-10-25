package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) {
        //1.创建Semaphore对象
        Semaphore semaphore = new Semaphore(3);   //许可为3，表示只能有三个线程可以访问
        //2.创建10个线程来使用
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    //semaphore --
                    semaphore.acquire();
                    log.debug("running....");
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("end....");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    //semaphore ++
                    semaphore.release();
                }

            }).start();
        }
    }
}
