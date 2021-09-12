package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

/**
 * 交替输出a,b,c
 * 输出内容     等待标记        下个标记
 *  a               1           2
 *  b               2           3
 *  c               3           1
 */
@Slf4j(topic = "c.AlternateOutput1")
public class AlternateOutput1 {
    private int flg;    //等待标记
    private int loopNum;    //循环次数

    public AlternateOutput1(int flg, int loopNum) {
        this.flg = flg;
        this.loopNum = loopNum;
    }

    /**
     * 打印
     * @param str 打印内容
     * @param waitFlg   等待标记
     * @param nextFlg   下个标记
     */
    public void printCharacter(String str,int waitFlg,int nextFlg){
        for (int i = 0; i < loopNum; i++) {
            synchronized (this){
                while (flg != waitFlg){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug(str);
                //叫醒下个线程
                flg = nextFlg;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        AlternateOutput1 alternateOutput1 = new AlternateOutput1(1, 3);
        new Thread(() -> {
            alternateOutput1.printCharacter("A",1,2);
        },"t1").start();

        new Thread(() -> {
            alternateOutput1.printCharacter("B",2,3);
        },"t2").start();

        new Thread(() -> {
            alternateOutput1.printCharacter("C",3,1);
        },"t3").start();
    }
}
