package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Number3")
class Number3{
    public synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("a");
    }

    public synchronized void b(){
        log.debug("b");
    }

    public void c(){
        log.debug("c");
    }
}

public class ThreadEightLockThree {
    public static void main(String[] args) {
        Number3 number = new Number3();
        new Thread(() -> {
            try {
                number.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> { number.b(); }).start();
        new Thread(() -> { number.c(); }).start();
    }
}
