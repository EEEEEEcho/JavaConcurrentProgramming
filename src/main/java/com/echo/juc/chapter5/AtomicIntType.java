package com.echo.juc.chapter5;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class AtomicIntType {
    public static void main(String[] args) {
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        atomicInteger.incrementAndGet();    //自增1 ++i 先自增再获取
//        System.out.println(atomicInteger);
//        System.out.println(atomicInteger.getAndIncrement());    //先获取在自增
//        System.out.println(atomicInteger.get());
//
//        System.out.println(atomicInteger.getAndAdd(5)); //先获取再增加5
//        System.out.println(atomicInteger.addAndGet(5)); //先增加5再获取

        AtomicInteger integer = new AtomicInteger(5);
        System.out.println(integer.updateAndGet(x -> x * 5));//先对值进行操作，然后再获取
        System.out.println(integer.getAndUpdate(x -> x * 2));
        System.out.println(integer.get());

        updateAndGetDemo(integer,p -> p / 2);
        System.out.println(integer.get());
    }

    //updateAndGet原理
    public static void updateAndGetDemo(AtomicInteger integer, IntUnaryOperator operator){
        while (true){
            int pre = integer.get();
            int next = operator.applyAsInt(pre);
            if (integer.compareAndSet(pre,next)){
                break;
            }
        }
    }
}
