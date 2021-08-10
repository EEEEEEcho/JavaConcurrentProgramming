package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Number5")
class Number5{
    public static synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
@Slf4j(topic = "c.ThreadEightLockFive")
public class ThreadEightLockFive {
    public static void main(String[] args) {
        Number5 number5 = new Number5();
        new Thread(()->{
            try {
                number5.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            number5.b();
        }).start();
    }
}
