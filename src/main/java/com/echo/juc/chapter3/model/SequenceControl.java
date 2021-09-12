package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制程序执行顺序，一定是先打印2后打印1
 */
@Slf4j(topic = "c.SequenceControl")
public class SequenceControl {
    static final Object lock = new Object();
    //表示t2是否执行过
    static boolean t2run = false;
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock){
                while (!t2run){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock){
                log.debug("2");
                t2run = true;
                lock.notifyAll();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
