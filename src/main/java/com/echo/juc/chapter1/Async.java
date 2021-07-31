package com.echo.juc.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j(topic = "c.Async")
public class Async {
    public static void main(String[] args){
        new Thread(() -> {
            log.debug("start sleep");
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end sleep");
        }).start();
        log.debug("do other thing");
    }
}
