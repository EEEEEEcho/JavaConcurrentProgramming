package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.BiasedTest2")
public class BiasedTest2 {
    public static void main(String[] args) {
        Dog lock = new Dog();
        //间隔使用锁对象时，导致偏向锁转换为轻量级锁
        new Thread(() ->{
            log.info("加锁前");
            String s1 = ClassLayout.parseInstance(lock).toPrintable();
            log.debug(s1);
            synchronized (lock){
                log.info("加锁中");
                String s2 = ClassLayout.parseInstance(lock).toPrintable();
                log.debug(s2);
            }
            log.info("加锁后");
            String s3 = ClassLayout.parseInstance(lock).toPrintable();
            log.debug(s3);

            //为了保证两个线程先后执行，采取notify的操作
            synchronized (BiasedTest2.class){
                BiasedTest2.class.notify();
            }

        },"t1").start();

        new Thread(() ->{
            //为了保证两个线程先后执行，采取wait的操作
            synchronized (BiasedTest2.class){
                try {
                    BiasedTest2.class.wait();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            log.info("加锁前");
            String s1 = ClassLayout.parseInstance(lock).toPrintable();
            log.debug(s1);
            synchronized (lock){
                log.info("加锁中");
                String s2 = ClassLayout.parseInstance(lock).toPrintable();
                log.debug(s2);
            }
            log.info("加锁后");
            String s3 = ClassLayout.parseInstance(lock).toPrintable();
            log.debug(s3);
        },"t2").start();

    }
}
