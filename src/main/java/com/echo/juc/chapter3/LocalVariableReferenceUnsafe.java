package com.echo.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j(topic = "c.LocalVariableReferenceUnsafe")
public class LocalVariableReferenceUnsafe {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadUnsafe th = new ThreadUnsafe();
        for (int i = 0;i < THREAD_NUMBER;i ++){
            new Thread(() -> {
                th.method1(LOOP_NUMBER);
            },"Thread" + i).start();
        }
    }
}
class ThreadUnsafe{
    ArrayList<String> arrayList = new ArrayList<>();
    public void method1(int loop){
        for (int i = 0;i < loop;i ++){
            method2();
            method3();
        }
    }

    public void method2(){
        arrayList.add("1");
    }

    public void method3(){
        arrayList.remove(0);
    }
}