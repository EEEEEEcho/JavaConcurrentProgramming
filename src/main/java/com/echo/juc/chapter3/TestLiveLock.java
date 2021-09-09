package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.TestLiveLock")
public class TestLiveLock {
    static volatile int count = 10;

    public static void main(String[] args) {
        new Thread(() ->{
            while (count > 0){
                //将count减到0结束
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count --;
                log.debug("count:{}",count);
            }
        },"t1").start();

        new Thread(()->{
            while (count < 20){
                //将count加到20结束
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count ++;
                log.debug("count:{}",count);
            }
        },"t2").start();
    }
}
