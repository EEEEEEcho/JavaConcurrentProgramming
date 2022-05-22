package com.echo.review;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            System.out.println("task1 task2 finish...");
        });
        new Thread(() -> {
            try {
                System.out.println("这是线程1");
                TimeUnit.SECONDS.sleep(1);
                barrier.await();    //等线程2执行完  然后执行构造方法里面的任务
                System.out.println("线程1结束了");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("这是线程2");
                TimeUnit.SECONDS.sleep(2);
                barrier.await();
                System.out.println("线程2结束了");
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
