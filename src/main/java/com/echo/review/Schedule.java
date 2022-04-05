package com.echo.review;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Schedule {
    public static void main(String[] args) {
//        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
//        pool.schedule(() -> {
//            System.out.println("Hello");
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            }
//            catch(InterruptedException exception){
//                exception.printStackTrace();
//            }
//            System.out.println("world");
//        },1,TimeUnit.SECONDS);
//
//        pool.schedule(() ->{
//            System.out.println("你好");
//            try{
//                TimeUnit.SECONDS.sleep(2);
//            }
//            catch (InterruptedException exception){
//                exception.printStackTrace();
//            }
//            System.out.println("世界");
//        },1,TimeUnit.SECONDS);
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
//        pool.scheduleAtFixedRate(() -> {
//            System.out.println("Hello world");
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
//        },1,2,TimeUnit.SECONDS);
        //pool.shutdown();
        pool.scheduleWithFixedDelay(() -> {
            System.out.println("Hello world");
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        },1,3,TimeUnit.SECONDS);
    }
}
