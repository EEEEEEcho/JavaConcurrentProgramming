package com.echo.review;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Review1")
public class Review1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Thread thread = new Thread(() -> {
//            System.out.println("start");
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("park");
//            LockSupport.park();
//            System.out.println("CONTINUE");
//        });
//        thread.start();
//        TimeUnit.SECONDS.sleep(3);
//        LockSupport.unpark(thread);
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        People echo = new People("Echo",c1,c2);
        People ekko = new People("Ekko", c2, c3);
        People jane = new People("Jane", c3, c4);
        People lucky = new People("Lucky", c4, c5);
        People child = new People("Child", c5, c1);
        echo.start();
        ekko.start();
        jane.start();
        lucky.start();
        child.start();
    }
}
class Guard{
    private Object response;

    public Object getResponse(long timeout){
        synchronized (this){
            //开始时间
            long begin = System.currentTimeMillis();
            //经历的时间
            long passedTime = 0;
            while (response == null){
                if (passedTime >= timeout){
                    break;
                }
                try {
                    this.wait(timeout - passedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }
    public void setResponse(Object o){
        synchronized (this){
            this.response = o;
            this.notifyAll();
        }
    }
}

class Chopstick extends ReentrantLock {
    private String name;
    public Chopstick(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}
class People extends Thread{
    private Chopstick left;
    private Chopstick right;
    public People(String name,Chopstick left,Chopstick right){
        super(name);
        this.left = left;
        this.right = right;
    }
    @Override
    public void run() {
//        synchronized (left){
//            System.out.println(getName() + "拿到了左边筷子" + left);
//            synchronized (right){
//                System.out.println(getName() + "拿到了右边筷子" + right);
//                System.out.println(getName() + "吃他妈的");
//            }
//        }
        while (true){
            if (left.tryLock()){
                try{
                    System.out.println(getName() + "拿到了左边筷子" + left);
                    if (right.tryLock()){
                        try {
                            System.out.println(getName() + "拿到了右边筷子" + right);
                            System.out.println(getName() + "吃他妈的");
                            break;
                        }
                        finally {
                            right.unlock();
                        }

                    }
                }
                finally {
                    left.unlock();
                }
            }
        }
    }
}