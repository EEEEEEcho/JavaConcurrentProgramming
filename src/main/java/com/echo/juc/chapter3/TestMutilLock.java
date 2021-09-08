package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j(topic = "c.TestMutilLock")
public class TestMutilLock {
    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            try {
                bigRoom.sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"小南").start();

        new Thread(() -> {
            try {
                bigRoom.study();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"小女").start();
    }
}

@Slf4j(topic = "c.BigRoom")
class BigRoom{
    //使用多把锁，解耦不同的功能
    private final Object studyRoom = new Object();
    private final Object sleepRoom = new Object();

    public void sleep() throws InterruptedException {
        synchronized (sleepRoom){
            log.debug("睡觉2小时");
            TimeUnit.SECONDS.sleep(2);
        }
    }

    public void study() throws InterruptedException{
        synchronized (studyRoom){
            log.debug("学习1小时");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}