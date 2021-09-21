package com.echo.juc.chapter4.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 犹豫模式:犹豫就体现在，如果已经有一个线程调用过start了，那么另一个线程就没必要调用了
 * 即：做某件事情之前，犹豫一下判断其他人是不是已经做过了
 */
@Slf4j(topic = "c.HesitationMode")
public class HesitationMode1 {
    public static void main(String[] args) {
        TwoParseTermination t = new TwoParseTermination();
        t.start();
        t.start();
    }
}

@Slf4j(topic = "c.TwoParse")
class TwoParse {
    //监控线程
    private Thread monitor;
    //停止标记
    private volatile boolean stop = false;
    //线程是否执行过start
    private volatile boolean starting = false;

    //启动监控线程
    public void start() {
        synchronized (this) {
            if (starting) {
                return;
            }
            starting = true;
        }
        monitor = new Thread(() -> {
            while (true){
                Thread current = Thread.currentThread();
                if (stop){
                    log.debug("料理后事");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (Exception e){
                    current.interrupt();
                    e.printStackTrace();
                }
                log.debug("执行监控程序");
            }
        },"monitor");
        monitor.start();
    }


    public void stop() {
        stop = true;
        monitor.interrupt();
    }
}
