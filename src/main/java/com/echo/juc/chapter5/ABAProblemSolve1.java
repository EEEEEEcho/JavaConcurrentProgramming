package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j(topic = "c.ABAProblemSolve1")
public class ABAProblemSolve1 {
    //给一个初始的值以及一个初始的版本号
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("Main start....");
        //获取值A
        String pre = ref.getReference();
        //获取版本号
        int stamp = ref.getStamp();
        //打印
        log.debug("value:{},stamp:{}",pre,stamp);


        doOtherThing();
        TimeUnit.SECONDS.sleep(1);
        //更新值的时候，不仅要更新值，也要更新版本号。
        log.debug("value:{},stamp:{}",ref.getReference(),ref.getStamp());
        log.debug("change A->C : {}",ref.compareAndSet(pre,"C",stamp,stamp + 1));
    }

    public static void doOtherThing(){
        new Thread(() -> {
            int stamp = ref.getStamp();
            String pre = ref.getReference();
            log.debug("value:{},stamp:{}",pre,stamp);
            log.debug("change A->B : {}",ref.compareAndSet(pre, "B",stamp,stamp + 1));
        },"t1").start();

        new Thread(() -> {
            int stamp = ref.getStamp();
            String pre = ref.getReference();
            log.debug("value:{},stamp:{}",pre,stamp);
            log.debug("change B->A : {}",ref.compareAndSet(pre, "A",stamp,stamp + 1));
        },"t2").start();
    }
}
