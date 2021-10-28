package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestStampedLock")
public class TestStampedLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainerStamped container = new DataContainerStamped(1);
        new Thread(() -> {
            try {
                container.read(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(() -> {
            try {
                container.write(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}
