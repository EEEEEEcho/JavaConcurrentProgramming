package com.echo.juc.chapter4;

import java.util.concurrent.TimeUnit;

public class VisibilityIssues2 {
    static boolean run = true;
    final static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(run){
                synchronized (lock){
                    if (!run){
                        break;
                    }
                    int i = 0;
                }
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(1);
        synchronized (lock){
            run = false;
        }
    }
}
