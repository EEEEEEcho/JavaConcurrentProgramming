package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ABADemo {
    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {
        String pre = ref.get();
        doOther();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("A -> C : " + ref.compareAndSet("A","C"));
    }
    public static void doOther(){
        new Thread(() -> {
            System.out.println("A -> B : " + ref.compareAndSet("A","B"));
        }).start();

        new Thread(() -> {
            System.out.println("B -> A : " + ref.compareAndSet("B","A"));
        }).start();
    }
}
