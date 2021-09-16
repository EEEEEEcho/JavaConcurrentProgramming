package com.echo.juc.chapter4.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TwoParseTerminationDemo")
public class TwoParseTerminationDemo {
    public static void main(String[] args) throws InterruptedException {
        TwoParseTermination t = new TwoParseTermination();
        t.start();
        t.start();
//        //主线程优雅的去停止该线程.
//        TimeUnit.SECONDS.sleep(3);
//        t.stop();
    }
}

@Slf4j(topic = "c.TwoParseTermination")
class TwoParseTermination{
    private Thread monitor;

    private volatile boolean stop = false;

    public void start(){
        monitor = new Thread(() -> {
            while (true){
                Thread current = Thread.currentThread();
                if (stop){
                    log.debug("料理后事");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (Exception e){
                    current.interrupt();
                    e.printStackTrace();
                }
                log.debug("执行监控程序");
            }
        },"monitor");
        monitor.start();
    }

    //停止监控线程,其他线程调用该方法，所以要保证这个stop变量的可见性
    //即其他线程调用该方法，修改stop的值之后，当前线程能够感知到
    public void stop(){
        stop = true;
        //防止调用该方法时，线程在休眠
        monitor.interrupt();
    }
}