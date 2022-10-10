package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  * 交替输出a,b,c
 *  * 输出内容     等待标记        下个标记
 *  *  a               1           2
 *  *  b               2           3
 *  *  c               3           1
 *      ReentryLock版
 */
@Slf4j(topic = "c.AlternateOutput2")
public class AlternateOutput2 {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal lock = new AwaitSignal(3);
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        Thread t1 = new Thread(() -> {
            lock.printCharacter("A",conditionA,conditionB);
        }, "t1");

        Thread t2 = new Thread(() -> {
            lock.printCharacter("B",conditionB,conditionC);
        }, "t2");

        Thread t3 = new Thread(() -> {
            lock.printCharacter("C",conditionC,conditionA);
        }, "t3");

        Thread t4 = new Thread(() -> {
            try {
                try {
                    lock.lock();
                    conditionC.await();
                    //不用显示的去调用lock，在被唤醒后，也会主动去竞争锁。并非不主动竞争锁，就不会竞争锁了
                    log.debug("没想到吧 hhhhh");
                }
                finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t4");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        //主线程发起开始的命令，因为这三个都进入休息室了
        TimeUnit.SECONDS.sleep(1);
        lock.lock();
        try{
            log.debug("开始。。。");
            //叫醒A
            conditionA.signal();
        }
        finally {
            lock.unlock();
        }
    }
}
@Slf4j(topic = "c.AwaitSignal")
class AwaitSignal extends ReentrantLock{
    private int loopNum;
    public AwaitSignal(int loopNum) {
        this.loopNum = loopNum;
    }

    public void printCharacter(String str,Condition current,Condition next){
//        try {
//            this.lock();
//            for (int i = 0; i < loopNum; i++) {
//                current.await();
//                log.debug(str);
//                next.signal();
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        finally {
//            this.unlock();
//        }
        for (int i = 0; i < loopNum; i++) {
            this.lock();
            try {
                current.await();
                log.debug(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }
}

