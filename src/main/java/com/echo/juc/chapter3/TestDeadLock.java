package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestDeadLock")
public class TestDeadLock {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Thread t1 = new Thread(()->{
            synchronized (lock1){
                log.debug("获取了lock1的锁");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2){
                    log.debug("获取了lock2的锁");
                    log.debug("开始操作");
                }
            }
        },"t1");
        Thread t2 = new Thread(()->{
            synchronized (lock2){
                log.debug("获取了lock2的锁");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1){
                    log.debug("获取了lock1的锁");
                    log.debug("开始操作");
                }
            }
        },"t2");

        t1.start();
        t2.start();
    }
}
