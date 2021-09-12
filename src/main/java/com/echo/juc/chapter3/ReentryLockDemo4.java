package com.echo.juc.chapter3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentryLockDemo4 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        //创建一个新的条件变量,（一个新的休息室）
        Condition condition1 = lock.newCondition();
        //再创建一个新的条件变量，(另一个休息室)
        Condition condition2 = lock.newCondition();
        //当前线程进入休息室等待
        condition1.await();

        //唤醒休息室1中的某个线程，类似notify()
        condition1.signal();
        //唤醒休息室1中的所有线程,类似notifyAll()
        condition1.signalAll();
    }
}
