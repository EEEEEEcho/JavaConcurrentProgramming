package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的可打断特性
 */
@Slf4j(topic = "c.ReentryLockDemo2")
public class ReentryLockDemo2 {
    private static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                //如果没有竞争，那么该方法和.lock()方法一样，获取lock对象的锁
                //如果有竞争，可以进入阻塞队列，但是在阻塞队列中时，其他线程可以使用interrupt方法将其等待状态打断
                log.debug("尝试获得锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.debug("被打断了，没有获得锁，直接返回");
                e.printStackTrace();
                return;
            }
            try {
                log.debug("获取到锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();    //主线程先获得锁。这样t1就获得不了锁
        t1.start();

        //睡一秒
        TimeUnit.SECONDS.sleep(1);
        log.debug("打断t1");
        t1.interrupt();
    }
}
