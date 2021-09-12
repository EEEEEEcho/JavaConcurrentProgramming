package com.echo.juc.chapter3.model;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestDemo {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            lock.lock();
            try {
                condition.await();
                System.out.println("继续执行...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        lock.lock();
        try {
            condition.signal();
        }
        finally {
            lock.unlock();
        }
    }
}
