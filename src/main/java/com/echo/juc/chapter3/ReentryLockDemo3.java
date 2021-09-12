package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的锁超时特性
 */
@Slf4j(topic = "c.ReentryLockDemo3")
public class ReentryLockDemo3 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("尝试获得锁");
            boolean success = false;//获取成功 返回 true,获取失败 返回 false
            try {
                success = lock.tryLock(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!success) {
                log.debug("获取不到锁");
                return;
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        log.debug("主线程获得了锁");
        lock.lock();
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        lock.unlock();
    }
}
