package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.ObserveMultiThread")
public class ObserveMultiThread {
    public static void main(String[] args) {
        new Thread(() -> {
            while (true){
                log.debug("Running t1");
            }
        },"t1").start();

        new Thread(() -> {
            while (true){
                log.debug("Running t2");
            }
        },"t2").start();
    }
}
