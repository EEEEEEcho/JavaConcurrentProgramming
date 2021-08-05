package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.SixState")
public class SixState {
    public static void main(String[] args) {
        //t1新建出来还没有start
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("t1 running...");
            }
        };

        //t2一直执行
        Thread t2 = new Thread("t2"){
            @Override
            public void run() {
                while (true){

                }
            }
        };
        t2.start();

        //t3正常执行并结束
        Thread t3 = new Thread("t3"){
            @Override
            public void run() {
                log.debug("t3 running...");
            }
        };
        t3.start();

        //t4对SixState.class加锁（可以拿到），然后执行休眠
        Thread t4 = new Thread("t4"){
            @Override
            public void run() {
                synchronized (SixState.class){
                    try {
                        TimeUnit.SECONDS.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        //t5 等待t2执行完毕，因为t2是死循环，所以一直不会执行完毕
        Thread t5 = new Thread("t5"){
            @Override
            public void run() {
                try{
                    t2.join();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        //t6等待拿到SixState.class的锁，但是由于t4没有休眠完成，所以不会得到锁
        Thread t6 = new Thread("t6"){
            @Override
            public void run() {
                //这里t6是拿不到锁的，所以会一直等待锁
                synchronized (SixState.class){
                    //blocked
                    try {
                        TimeUnit.SECONDS.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t6.start();

        log.debug("t1 state {}", t1.getState());
        log.debug("t2 state {}", t2.getState());
        log.debug("t3 state {}", t3.getState());
        log.debug("t4 state {}", t4.getState());
        log.debug("t5 state {}", t5.getState());
        log.debug("t6 state {}", t6.getState());
    }
}
