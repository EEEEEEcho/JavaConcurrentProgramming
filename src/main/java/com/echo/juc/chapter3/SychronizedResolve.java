package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.SychronizedResolve")
public class SychronizedResolve {
    static int count = 0;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (lock){   //拿到锁对象，锁住代码
                    count ++;
                }
            }
        },"t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock){
                    count--;
                }
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("count :{}",count);
    }
}
