package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j(topic = "c.Number4")
class Number4{
    public synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
@Slf4j(topic = "c.ThreadEightLockFour")
public class ThreadEightLockFour {
    public static void main(String[] args) {
        Number4 n1 = new Number4();
        Number4 n2 = new Number4();

        new Thread(()->{
            try {
                n1.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            n2.b();
        }).start();

    }
}
