package com.echo.review;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
//        CountDownLatch countDownLatch = new CountDownLatch(3);
//        new Thread(() -> {
//            System.out.println("T1 Begin...");
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
//            System.out.println("T1 End...");
//            countDownLatch.countDown();
//        }).start();
//
//        new Thread(() -> {
//            System.out.println("T2 Begin");
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
//            System.out.println("T2 End...");
//            countDownLatch.countDown();
//        }).start();
//
//        new Thread(() -> {
//            System.out.println("T3 Begin");
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
//            System.out.println("T3 End..");
//            countDownLatch.countDown();
//        }).start();
//        countDownLatch.await();
//        System.out.println("Over.");
        printABC();
    }
    public static void printABC(){
        CountDownLatch latch = new CountDownLatch(4);
        Thread t1 = new Thread(new PrintTool(1,latch));
        Thread t2 = new Thread(new PrintTool(2,latch));
        Thread t3 = new Thread(new PrintTool(3,latch));
        t1.start();
        t2.start();
        t3.start();
        try {
            latch.countDown();
            latch.await();
            System.out.println("结束");
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
class PrintTool implements Runnable{
    private int waitTime;
    private CountDownLatch latch;
    public PrintTool(int waitTime,CountDownLatch latch){
        this.waitTime = waitTime;
        this.latch = latch;
    }
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(waitTime);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        System.out.println("完成了任务: " + waitTime);
        latch.countDown();
    }
}