package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的可重入特性
 */
@Slf4j(topic = "c.ReentryLockDemo1")
public class ReentryLockDemo1 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        method1();
    }

    public static void method1(){
        lock.lock();    //这相当于synchronized的开始部分，中间部分加锁
        try {
            log.debug("execute method1");
            method2();
        }
        finally {
            lock.unlock();//这相当于synchronized的结束部分
        }
    }
    public static void method2(){
        lock.lock();    //对同一个锁对象lock 进行加锁
        try {
            log.debug("execute method2");
            method3();
        }
        finally {
            lock.unlock();
        }
    }

    public static void method3(){
        lock.lock();
        try {
            log.debug("execute method3");
        }
        finally {
            lock.unlock();
        }
    }
}
