package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestWaitNotify")
public class TestWaitNotify {
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock){
                log.debug("执行...");
                try {
                    lock.wait();    //线程t1在lock的monitor的wait-set上等下去
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                log.debug("其他代码...");   //断点
            }
        },"t1").start();

        new Thread(() -> {
            synchronized (lock){
                log.debug("执行...");
                try {
                    lock.wait();    //线程t2在lock的monitor的wait-set上等下去
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                log.debug("其他代码...");   //断点
            }
        },"t2").start();

        //主线程两秒后运行
        TimeUnit.SECONDS.sleep(2);
        log.debug("唤醒lock上的其他线程");
        synchronized (lock){
            lock.notifyAll();   //断点
        }
    }
}
