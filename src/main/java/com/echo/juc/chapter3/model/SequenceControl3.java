package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * park&unpark版，顺序控制
 */
@Slf4j(topic = "c.SequenceControl3")
public class SequenceControl3 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        },"t1");

        Thread t2 = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        },"t2");
        t1.start();
        t2.start();
    }
}
