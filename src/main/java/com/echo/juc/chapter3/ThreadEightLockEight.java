package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Number8")
class Number8{
    public static synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public static synchronized void b()  {
        log.debug("2");
    }
}

@Slf4j(topic = "c.ThreadEightLockEight")
public class ThreadEightLockEight {
    public static void main(String[] args) {
        Number8 num1 = new Number8();
        Number8 num2 = new Number8();
        new Thread(()->{
            try {
                num1.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            num2.b();
        }).start();
    }
}

