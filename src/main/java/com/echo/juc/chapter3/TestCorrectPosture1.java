package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestCorrectPosture1")
public class TestCorrectPosture1 {
    static final Object room = new Object();
    static boolean hacCigarette = false;    //有烟吗？
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room){
                log.debug("有烟没?[{}]",hacCigarette);
                if (!hacCigarette){
                    log.debug("没烟，先歇会");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没?[{}]",hacCigarette);
                if (hacCigarette){
                    log.debug("可以开始干活了");
                }
                else {
                    log.debug("还是么有，老子不干了");
                }
            }
        },"小南").start();

        //其余五个人等着拿到房间干活
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room){
                    log.debug("可以开始干活了");
                }
            },"其他人").start();
        }
        //主线程一秒之后派个送烟的过去
        TimeUnit.SECONDS.sleep(1);
        //送烟的
        new Thread(() -> {
            hacCigarette = true;
            log.debug("烟送到了");
        },"送烟的").start();
    }
}
