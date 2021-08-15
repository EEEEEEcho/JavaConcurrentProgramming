package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.BiasedTest")
public class BiasedTest {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        log.debug("延迟前");
        String s = ClassLayout.parseInstance(dog).toPrintable();
        log.debug(s);
        Thread.sleep(4000);
        Dog newDog = new Dog();
        //hashcode() 方法会禁用偏向锁。
        newDog.hashCode();
        //加锁前打印
        log.info("加锁前");
        String s1 = ClassLayout.parseInstance(newDog).toPrintable();
        log.debug(s1);
        //加锁后打印
        synchronized (newDog){
            log.debug("加锁中");
            String s2 = ClassLayout.parseInstance(newDog).toPrintable();
            log.debug(s2);
        }
        //解锁后打印
        log.debug("枷锁后");
        String s2 = ClassLayout.parseInstance(newDog).toPrintable();
        log.debug(s2);
    }
}
class Dog{

}
