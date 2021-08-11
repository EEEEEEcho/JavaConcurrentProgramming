package com.echo.juc.chapter3;

import java.util.ArrayList;

public class LocalVariableReferenceSafe {
    static final int THREAD_NUM = 2;
    static final int LOOP = 200;
    public static void main(String[] args) {
        ThreadSafe threadSafe = new ThreadSafe();
        for (int i = 0;i < THREAD_NUM;i ++){
            new Thread(() -> {
                threadSafe.method1(LOOP);
            },"Thread" + (i + 1)).start();
        }
    }
}
class ThreadSafe{
    public void method1(int loop){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0;i < loop;i ++){
            method2(list);
            method3(list);
        }
    }

    public void method2(ArrayList<String> list){
        list.add("1");
    }

    public void method3(ArrayList<String> list){
        list.remove(0);
    }
}