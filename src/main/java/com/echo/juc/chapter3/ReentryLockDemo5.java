package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ReentryLockDemo5")
public class ReentryLockDemo5 {
    static final Object room = new Object();
    static boolean hacCigarette = false;    //有烟吗？
    static boolean hasTakeout = false;      //有外卖吗？

    static ReentrantLock mainRoom = new ReentrantLock();//主房间
    static Condition cigaretteRoom = mainRoom.newCondition();   //主房间划出一个抽烟的房间
    static Condition takeOutRoom = mainRoom.newCondition();     //主房间划出一个吃外卖的房间

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            mainRoom.lock();
            try {
                log.debug("有烟没?[{}]",hacCigarette);
                //使用while循环进行改造
                while (!hacCigarette){
                    log.debug("没烟，先歇会");
                    try {
                        cigaretteRoom.await();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //被叫醒之后执行下面的代码
                log.debug("有烟没?[{}]",hacCigarette);
                if (hacCigarette){
                    log.debug("可以开始干活了");
                }
                else {
                    log.debug("还是么有，老子不干了");
                }
            }
            finally {
                mainRoom.unlock();
            }
        },"小南").start();

        new Thread(() -> {
            mainRoom.lock();
            try {
                log.debug("有外卖吗?[{}]",hasTakeout);
                if (!hasTakeout){
                    log.debug("没外卖，先歇会");
                    try {
                        takeOutRoom.await();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //被叫醒之后执行下面的代码
                log.debug("有外卖没?[{}]",hasTakeout);
                if (hasTakeout){
                    log.debug("可以开始干活了");
                }
                else {
                    log.debug("还是么有，老子不干了");
                }
            }
            finally {
                mainRoom.unlock();
            }
        },"小女").start();

        //主线程一秒之后派个外卖的过去
        TimeUnit.SECONDS.sleep(1);
        //送外卖
        new Thread(() -> {
            mainRoom.lock();
            try {
                hasTakeout = true;
                log.debug("外卖送到了");
                //叫醒外卖房间的线程
                takeOutRoom.signalAll();
            }
            finally {
                mainRoom.unlock();
            }
        },"送外卖的").start();

        TimeUnit.SECONDS.sleep(1);
        //送烟
        new Thread(() -> {
            mainRoom.lock();
            try {
                hacCigarette = true;
                log.debug("烟送到了");
                //叫醒外卖房间的线程
                cigaretteRoom.signalAll();
            }
            finally {
                mainRoom.unlock();
            }
        },"送烟的").start();
    }
}
