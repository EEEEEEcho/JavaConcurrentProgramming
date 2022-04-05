package com.echo.review;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintDemo3 {
    public static boolean flg1 = false;
    public static boolean flg2 = false;
    public static boolean flg3 = false;
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition c1 = lock.newCondition();
    public static Condition c2 = lock.newCondition();
    public static Condition c3 = lock.newCondition();
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try{
                    while (!flg1){
                        try {
                            c1.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("A");
                    flg2 = true;
                    c2.signal();
                    flg1 = false;
                }
                finally {
                    lock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    while (!flg2){
                        try {
                            c2.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("B");
                    flg3 = true;
                    c3.signal();
                    flg2 = false;
                }
                finally {
                    lock.unlock();
                }
            }

        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    while (!flg3){
                        try {
                            c3.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("c");
                    flg1 = true;
                    c1.signal();
                    flg3 = false;
                }
                finally {
                    lock.unlock();
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
        flg1 = true;
    }
}
