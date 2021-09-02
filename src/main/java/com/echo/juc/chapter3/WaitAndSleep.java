package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.WaitAndSleep")
public class WaitAndSleep {
    //定义为final，防止引用的修改
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock){
                try{
                    //睡眠的这两秒，其他线程是不能获得锁的
                    //Thread.sleep(2000);
                    //调用lock.wait会让当前拿到锁的线程去monitor的waitset中阻塞timeout的时间
                    //同时释放锁，让其他线程可以拿到
                    lock.wait(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"t1").start();
        TimeUnit.SECONDS.sleep(1);
        synchronized (lock){
            log.debug("获得锁");
        }
    }
}
