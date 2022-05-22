package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Review {
//    static final Object lock = new Object();
//    static boolean ok = false;
    public static void main(String[] args) throws InterruptedException {
//         new Thread(() -> {
//             synchronized (lock){
//                 if (!ok){
//                     try {
//                         lock.wait();
//                     } catch (InterruptedException e) {
//                         e.printStackTrace();
//                     }
//                 }
//                 System.out.println("ok");
//             }
//         }).start();
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        synchronized (lock){
//            ok = true;
//            lock.notify();
//        }
//        Thread thread = new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//        thread.join();
//        System.out.println("Hello");
        Thread t1 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                System.out.println("我被打断了");
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        });
        t1.start();
        lock.lock();
        System.out.println("主线程获得了锁");
        t1.interrupt();
        System.out.println("t1被打断了");
    }
    static ReentrantLock lock = new ReentrantLock();
}
