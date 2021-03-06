package com.echo.juc.chapter3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家就餐问题
 */
public class PhilosopherMeal {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }
}

/**
 * 普通版本的筷子
 */
//@Slf4j(topic = "c.Chopstick")
//class Chopstick{
//    String name;
//
//    public Chopstick(String name){
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return "筷子{" +
//                "name='" + name + '\'' +
//                '}';
//    }
//}

/**
 * 继承了ReentrantLock之后的筷子
  */
@Slf4j(topic = "c.Chopstick")
class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" +
                "name='" + name + '\'' +
                '}';
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread{
    Chopstick left;
    Chopstick right;

    public Philosopher(String name,Chopstick left,Chopstick right){
        super(name);
        this.left = left;
        this.right = right;
    }

    public void eat() throws InterruptedException {
        log.debug(getName() + "eating...");
        TimeUnit.SECONDS.sleep(1);
    }

    @SneakyThrows   //将当前方法抛出的异常，包装成RuntimeException，骗过编译器，使得调用点可以不用显示处理异常信息。
    @Override
    public void run() {
        //使用synchronized 以及传统筷子导致死锁
//        while (true){
//            synchronized (left){
//                log.debug(getName() + "拿到了左边的筷子");
//                synchronized (right){
//                    log.debug(getName() + "拿到了右边的筷子");
//                    eat();
//                }
//            }
//        }
        //使用继承了ReentrantLock之后的筷子
        while (true){
            //尝试获取左手筷子
            if (left.tryLock()){
                //获取到锁之后，要加try-finally块。来释放锁
                try {
                    //尝试获取右手筷子
                    if (right.tryLock()){
                        //如果右手的筷子也获取到了，那么就吃饭
                        try {
                            eat();
                        }
                        finally {
                            right.unlock();     //释放右手的筷子
                        }
                    }
                }
                finally {
                    left.unlock();//释放左手的筷子
                }
            }
        }
    }
}