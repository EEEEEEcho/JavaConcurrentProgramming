package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestCorrectPosture4")
public class TestCorrectPosture4 {
    static final Object room = new Object();
    static boolean hacCigarette = false;    //有烟吗？
    static boolean hasTakeout = false;      //有外卖吗？

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room){
                log.debug("有烟没?[{}]",hacCigarette);
                //使用while循环进行改造
                while (!hacCigarette){
                    log.debug("没烟，先歇会");
                    //使用wait改造
                    try {
                        //没有拿到烟，小南线程进入等待队列，释放room锁
                        room.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //被叫醒之后执行下面的代码
                log.debug("有烟没?[{}]",hacCigarette);
                if (hacCigarette){
                    log.debug("可以开始干活了");
                }
                else {
                    log.debug("还是么有，老子不干了");
                }
            }
        },"小南").start();

        new Thread(() -> {
            synchronized (room){
                log.debug("有外卖吗?[{}]",hasTakeout);
                if (!hasTakeout){
                    log.debug("没外卖，先歇会");
                    //使用wait改造
                    try {
                        //没有拿到外卖，小女线程进入等待队列，释放room锁
                        room.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //被叫醒之后执行下面的代码
                log.debug("有外卖没?[{}]",hasTakeout);
                if (hasTakeout){
                    log.debug("可以开始干活了");
                }
                else {
                    log.debug("还是么有，老子不干了");
                }
            }
        },"小女").start();

        //主线程一秒之后派个外卖的过去
        TimeUnit.SECONDS.sleep(1);
        //送外卖
        new Thread(() -> {
            synchronized (room){
                hasTakeout = true;
                log.debug("外卖送到了");
                //叫醒wait-set中的线程
                room.notifyAll();
            }

        },"送外卖的").start();
    }
}
