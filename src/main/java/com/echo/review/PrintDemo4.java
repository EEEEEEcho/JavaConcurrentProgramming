package com.echo.review;

import java.util.concurrent.locks.LockSupport;

public class PrintDemo4 {
    public static String message = "Hello world";
    public static int index = 0;
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        UnparkPrint print = new UnparkPrint("Hel");
        t1 = new Thread(() -> {
            print.print(t2);
        });
        t2 = new Thread(() -> {
            print.print(t3);
        });
        t3 = new Thread(() -> {
            print.print(t1);
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
    public static void print(Thread current,Thread next){
        LockSupport.park(current);
        if (index < message.length()){
            System.out.println(message.charAt(index++));
        }
        LockSupport.unpark(next);
    }
}
class UnparkPrint{
    private String message;
    private int index;
    public UnparkPrint(String message){
        this.message = message;
    }
    public void print(Thread t){
        while (index < message.length()){
            LockSupport.park();
            System.out.println(message.charAt(index ++));
            LockSupport.unpark(t);
        }
    }
}
