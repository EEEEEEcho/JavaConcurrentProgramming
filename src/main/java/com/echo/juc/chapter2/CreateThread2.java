package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.CreateThread2")
public class CreateThread2 {
    public static void main(String[] args) {
//        //1.创建可执行的任务
//        Runnable runnable = new Runnable(){
//            @Override
//            public void run() {
//                log.debug("Running");
//            }
//        };
//        //2.创建线程对象并取名
//        Thread thread = new Thread(runnable, "runnable");
//        thread.start();
//        Runnable runnable = ()->{
//            log.debug("Running");
//        };
//        Thread thread = new Thread(runnable,"lambda");
//        thread.start();

        Thread thread = new Thread(()->{
            log.debug("Running");
        },"lambda");
        thread.start();

    }
}
