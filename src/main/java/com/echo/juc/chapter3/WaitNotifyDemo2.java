package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.WaitNotifyDemo2")
public class WaitNotifyDemo2 {
    final static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock){
                log.debug("执行....");
                try {
                    //调用这个方法，该线程t1就会跑到,lock的Monitor的waitset中
                    lock.wait();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                log.debug("其他代码。。。");
            }
        },"t1").start();


        new Thread(() -> {
            synchronized (lock){
                log.debug("执行....");
                try {
                    //调用这个方法，该线程t2就会跑到lock的Monitor的waitset中
                    lock.wait();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                log.debug("其他代码....");
            }
        },"t2").start();

        //主线程两秒后执行
        TimeUnit.SECONDS.sleep(2);
        log.debug("唤醒lock上其他线程");
        synchronized (lock){//必须获得锁，才能唤醒该锁的monitor的wait set的其他线程
            lock.notify();  //挑一个唤醒
            //lock.notifyAll(); //唤醒所有的
        }
    }
}
