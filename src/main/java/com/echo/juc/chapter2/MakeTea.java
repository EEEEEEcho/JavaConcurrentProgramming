package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.MakeTea")
public class MakeTea {
    public static void main(String[] args) {
        //t1.洗水壶，烧开水
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("洗水壶 1分钟");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("做开水 15分钟");
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //t2.洗茶杯，洗茶壶，拿茶叶
        Thread t2 = new Thread("t2"){
            @Override
            public void run() {
                log.debug("洗茶杯 1分钟");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("洗茶壶 2分钟");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("拿茶叶 1分钟");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //t2等待t1执行完成后，泡茶
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("泡茶");
            }
        };

        t1.start();
        t2.start();
    }
}
