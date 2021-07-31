package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.InterruptDemo2")
public class InterruptDemo2 {
    public static void main(String[] args) throws InterruptedException{
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                while (true) {
                    //在主线程调用t1.interrupted()方法之后，会将打断标记置为真，
                    //在该线程中，判断打断标记是否为真，如果为真，则自己将自己打断
                    boolean interrupted = Thread.currentThread().isInterrupted();
                    if (interrupted){
                        log.debug("被打断了，退出循环");
                        break;
                    }
                }
            }
        };
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("interrupt");
        //正常运行的线程，在调用interrupt()方法之后，并不会停止运行。而是将打断标记置为true
        //真正是否要停止运行，是通过判断打断标记，如果为真，就停止运行
        t1.interrupt();
    }
}
