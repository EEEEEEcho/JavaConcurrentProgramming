package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.LockParkDemo")
public class LockParkDemo {
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException{
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("park....");
                //锁住该线程，锁住之后，下面的代码不会执行
                LockSupport.park();
                log.debug("unpark...");
                log.debug("interrupt flg {}",Thread.currentThread().isInterrupted());
                //再锁一次,但是打断标记仍为真，所以不会锁住,只能在上面重置打断标记
                LockSupport.park();
                log.debug("unpark");
            }
        };
        t1.start();
        //主线程睡一秒
        TimeUnit.SECONDS.sleep(1);
        //打断,打断之后，打断标记为真
        t1.interrupt();
    }


}
