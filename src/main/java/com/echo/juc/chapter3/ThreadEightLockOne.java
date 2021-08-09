package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;
@Slf4j(topic = "c.Number")
class Number1{
    public synchronized void a(){
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}


@Slf4j(topic = "c.ThreadEightLockOne")
public class ThreadEightLockOne {
    public static void main(String[] args) {
        Number1 number = new Number1();
        new Thread(() -> { number.a(); }).start();
        new Thread(() -> { number.b(); }).start();
    }
}
