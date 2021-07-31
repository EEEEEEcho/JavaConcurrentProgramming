package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.CreateThread1")
public class CreateThread1 {
    public static void main(String[] args) {
        //1.创建线程
        Thread t = new Thread(){
            @Override
            public void run() {
                log.debug("Running");
            }
        };
        //2.指定名称并启动线程
        t.setName("log thread");
        t.start();
        //3.主线程中的打印
        log.debug("Main Running");
    }
}
