package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Number")
class Number2{
    public synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}


@Slf4j(topic = "c.ThreadEightLockTwo")
public class ThreadEightLockTwo {
    public static void main(String[] args) {
        Number2 number = new Number2();
        new Thread(() -> {
            try {
                number.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> { number.b(); }).start();
    }
}
