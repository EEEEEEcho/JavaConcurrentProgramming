package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintDemo {
    public static void main(String[] args) throws InterruptedException {
        SignalLock lock = new SignalLock(10);
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        Thread a = new Thread(() -> {
            lock.print("A", conditionA, conditionB);
        });
        Thread b = new Thread(() -> {
            lock.print("B", conditionB, conditionC);
        });
        Thread c = new Thread(() ->{
            lock.print("C",conditionC,conditionA);
        });

        a.start();
        b.start();
        c.start();
        TimeUnit.SECONDS.sleep(1);
        //为什么要对lock加锁呢？
        //是因为要想使用该lock的signal必须要先获得锁lock
        //就和直接使用notify一样。必须先获得锁，没有锁何谈notify
        //不限获得锁就直接调用notify()或者signal()会出现 java.lang.IllegalMonitorStateException错误
//        lock.notify();
//        conditionA.signal();
        lock.lock();
        try {
            conditionA.signal();
        }
        finally {
            lock.unlock();
        }
    }
}
class SignalLock extends ReentrantLock{
    private int loopNum;

    public SignalLock(int loopNum){
        this.loopNum = loopNum;
    }
    public void print(String str, Condition current,Condition next){
        for (int i = 0; i < loopNum; i++) {
            this.lock();
            try{
                current.await();
                System.out.println(str);
                next.signalAll();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            finally {
                this.unlock();
            }
        }
    }
}