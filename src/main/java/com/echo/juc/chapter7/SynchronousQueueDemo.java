package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.SynchronousQueueDemo")
public class SynchronousQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        new Thread(() -> {
            try{
                log.debug("putting : {}",1);
                queue.put(1);
                log.debug("{} putted",1);

                log.debug("putting : {}" , 2);
                queue.put(2);
                log.debug("{} putted",2);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        },"t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}",1);
                queue.take();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        },"t2").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}",2);
                queue.take();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        },"t3").start();
    }
}
