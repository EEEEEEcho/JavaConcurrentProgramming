package com.echo.review;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            int j = i;
            new Thread(() ->{
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("小猪佩奇: " + j);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
