package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.WaitNotifyDemo")
public class WaitNotifyDemo {
    static final Object lock = new Object();
    public static void main(String[] args) {
        //必须先获得了锁，线程才有资格进入Monitor的wait set中
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
