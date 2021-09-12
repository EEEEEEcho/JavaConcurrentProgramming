package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

public class AlternateOutput3 {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        ParkUnpark p = new ParkUnpark(3);
        t1 = new Thread(() -> {
            p.printCharacter("A",t2);
        },"t1");
        t2 = new Thread(() ->{
            p.printCharacter("B",t3);
        },"t2");
        t3 = new Thread(() ->{
            p.printCharacter("C",t1);
        },"t3");

        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }
}

@Slf4j(topic = "c.ParkUnpark")
class ParkUnpark{
    private int loopNum;

    public ParkUnpark(int loopNum) {
        this.loopNum = loopNum;
    }

    /**
     * 打印字符
     * @param str 要打印的内容
     * @param next 下一个要唤醒的线程
     */
    public void printCharacter(String str,Thread next){
        for (int i = 0; i < loopNum; i++) {
            LockSupport.park();
            log.debug(str);
            LockSupport.unpark(next);
        }
    }
}
