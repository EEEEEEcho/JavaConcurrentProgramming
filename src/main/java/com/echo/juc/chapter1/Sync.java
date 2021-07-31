package com.echo.juc.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Sync")
public class Sync {
    public static void main(String[] args) throws InterruptedException {
        log.debug("start sleep");
        TimeUnit.MILLISECONDS.sleep(2000);
        log.debug("end sleep");
        log.debug("do other thing");
    }
}
