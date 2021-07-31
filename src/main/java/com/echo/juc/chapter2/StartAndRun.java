package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.StartAndRun")
public class StartAndRun {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("t1 Running");
            }
        };
        log.debug(String.valueOf(t1.getState()));
        t1.start();
        log.debug("do other things");
        log.debug(String.valueOf(t1.getState()));
    }
}
