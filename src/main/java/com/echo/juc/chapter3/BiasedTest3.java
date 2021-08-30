package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

@Slf4j(topic = "c.BiasedTest3")
public class BiasedTest3 {

    public static void main(String[] args) {
        Vector<Dog> list = new Vector<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Dog d = new Dog();
                list.add(d);
                synchronized (d){
                    //这里30个dog对象都偏向了t1
                    log.debug(ClassLayout.parseInstance(d).toPrintable());
                }
            }
            synchronized (list){
                list.notify();
            }
        },"t1");
        t1.start();
        //t1执行完后，开始执行t2,t2将列表中偏向t1的30个对象都拿出来，执行一次加锁
        //在循环刚开始，会将偏向t1的偏向锁撤销，使用轻量级锁。
        //但是随着循环的次数增加，jvm会认为这样有问题，默认阈值是20，如果执行了20次之后，
        //还是t2来撤销t1的偏向锁进行加锁，那么jvm会直接将后续t2对偏向t1的对象的加锁设置为偏向锁
        //直接偏向给t2
        Thread t2 = new Thread(() -> {
            synchronized (list){
                try {
                    list.wait();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 30; i++) {
                Dog d = list.get(i);
                log.debug(ClassLayout.parseInstance(d).toPrintable());
                synchronized (d){
                    log.debug(ClassLayout.parseInstance(d).toPrintable());
                }
                log.debug(ClassLayout.parseInstance(d).toPrintable());
            }
        },"t2");
        t2.start();

    }
}
