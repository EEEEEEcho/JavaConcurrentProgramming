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
        t1.join();//主线程等待t1执行完成
        t2.join();//主线程等待t2执行完成
        log.debug("count :{}",count);
    }
}
