package com.echo.juc.chapter4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j(topic = "c.VisibilityIssues1")
public class VisibilityIssues1 {
    //volatile 本意为 易变的意思，加了这个关键字的变量，就不能从缓存中获取了，必须从主内存中读取
    volatile static boolean run = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(run){
                int i = 0;
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(1);
        run = false;
    }

}