package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
@Slf4j(topic = "c.ABAProblem")
public class ABAProblem {
    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {
        log.debug("Main start....");
        //获取值A
        //这个共享变量是否被其他线程修改过呢？
        String pre = ref.get();
        doOtherThing();
        TimeUnit.SECONDS.sleep(1);
        log.debug("change A->C : {}",ref.compareAndSet(pre,"C"));
    }

    public static void doOtherThing(){
        new Thread(() -> {
            log.debug("change A->B : {}",ref.compareAndSet(ref.get(), "B"));
        },"t1").start();

        new Thread(() -> {
            log.debug("change B->A : {}",ref.compareAndSet(ref.get(), "A"));
        },"t2").start();
    }
}
