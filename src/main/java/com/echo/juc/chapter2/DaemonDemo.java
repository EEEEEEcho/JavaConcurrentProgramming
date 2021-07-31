package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.DaemonDemo")
public class DaemonDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                while (true){
                    if(Thread.currentThread().isInterrupted()){
                        break;
                    }
                }
                log.debug("t1 end");
            }
        };
        //启动前设置为守护线程
        t1.setDaemon(true);
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("main end");
    }
}
