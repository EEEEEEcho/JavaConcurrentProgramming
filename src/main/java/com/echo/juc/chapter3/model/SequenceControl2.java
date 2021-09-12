package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentryLock版，顺序控制
 */
@Slf4j(topic = "c.SequenceControl2")
public class SequenceControl2 {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static boolean t2Run = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() ->{
            lock.lock();
            try{
                while (!t2Run){
                    //这是object的方法，ReentrantLock要实现wait/notify的效果，必须借助于Condition
                    //lock.wait();
                    condition.await();
                }
                log.debug("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t1");

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                log.debug("2");
                t2Run = true;
                //这是object的方法，ReentrantLock要实现wait/notify的效果，必须借助于Condition
                //lock.notifyAll();
                condition.signalAll();
            }
            finally {
                lock.unlock();
            }
        },"t2");

        t1.start();
        t2.start();
    }
}
