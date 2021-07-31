package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.PriorityTest")
public class PriorityTest {
    public static void main(String[] args) {
        Runnable task1 = ()->{
            int count = 0;
            for (;;){
                count ++;
                log.debug("t1 count {}",count);
            }
        };
        Runnable task2 = ()->{
            int count = 0;
            for (;;){
                count ++;
                log.debug("t2 count {}",count);
            }
        };

        Thread t1 = new Thread(task1,"t1");
        Thread t2 = new Thread(task2,"t2");
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t2.start();
    }
}
