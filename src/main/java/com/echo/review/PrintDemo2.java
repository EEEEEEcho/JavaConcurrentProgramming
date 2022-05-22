package com.echo.review;

import java.util.concurrent.locks.LockSupport;

public class PrintDemo2 {
    //提升作用域
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args){
        ParkPrint parkPrint = new ParkPrint(3);
        t1 = new Thread(() -> {
            parkPrint.print("A",t2);
        });
        t2 = new Thread(() -> {
            parkPrint.print("B",t3);
        });
        t3 = new Thread(() -> {
            parkPrint.print("C",t1);
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
}
class ParkPrint{
    private int loopNum;

    public ParkPrint(int loopNum){
        this.loopNum = loopNum;
    }

    public void print(String str,Thread t){
        for (int i = 0; i < loopNum; i++) {
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(t);
        }
    }
}
