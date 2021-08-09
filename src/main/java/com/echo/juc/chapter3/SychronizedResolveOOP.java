package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

/**
 * 以面向对象的方式改进
 */
@Slf4j(topic = "c.SychronizedResolveOOP")
public class SychronizedResolveOOP {

//    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        },"t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("count :{}",room.getCount());
    }
}
class Room{
    private int count = 0;
    public void increment(){
        //用当前对象自己保护原子操作
        synchronized (this){
            this.count ++;
        }
    }
    public void decrement(){
        synchronized (this){
            this.count -- ;
        }
    }
    public int getCount(){
        //为了保证获取时得到的是一个完整的结果，
        //而不是指令执行一半之后的结果，也需要加锁
        synchronized (this){
            return this.count;
        }
    }
}

