package com.echo.juc.chapter2;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TwoParseTermination")
public class TwoParseTerminationDemo {
    public static void main(String[] args) throws InterruptedException {
        TwoParseTermination t = new TwoParseTermination();
        t.start();
        //主线程优雅的去停止该线程.
        TimeUnit.SECONDS.sleep(3);
        t.stop();
    }
}

@Slf4j(topic = "c.TwoParseTermination")
class TwoParseTermination{
    private Thread monitor;

//    //启动监控线程
//    public void start(){
//        monitor = new Thread(){
//            @Override
//            public void run() {
//                while (true){
//                    boolean interrupted = Thread.currentThread().isInterrupted();
//                    //被打断
//                    if (interrupted){
//                        log.debug("料理后事");
//                        break;
//                    }
//                    //没被打断,睡眠一秒
//                    try {
//                        Thread.sleep(1000);
//                        log.debug("执行监控记录");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        //当睡眠时被打断。手动置打断标记为真
//                        //因为睡眠时并不会将打断标记置为true,因此需要手动设置打断标记为true
//                        //在这里可以设置成功是因为这里并没有sleep，而是正常的异常捕捉代码块
//                        Thread.currentThread().interrupt();
//                    }
//
//                }
//            }
//        };
//        monitor.start();
//    }
//
//    //停止监控线程
//    public void stop(){
//        monitor.interrupt();
//    }

    public void start(){
        monitor = new Thread(){
            @Override
            public void run() {
                while (true){
                    boolean flg = monitor.isInterrupted();
                    log.debug("打断标记是：" + flg);
                    if(flg){
                        log.debug("料理后市");
                        break;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        monitor.interrupt();
                        e.printStackTrace();
                    }
                    log.debug("执行监控任务...");
                }
            }
        };
        monitor.start();
    }
    public void stop(){
        monitor.interrupt();
    }
}