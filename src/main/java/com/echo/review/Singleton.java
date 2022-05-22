package com.echo.review;

import java.util.concurrent.atomic.LongAdder;

public final class Singleton {
    private static volatile Singleton INSTANCE = null;
    private Singleton(){

    }
    public static Singleton getInstance(){
        if(INSTANCE == null){
            synchronized (Singleton.class){
                if (INSTANCE == null){
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }

    public static void main(String[] args) {

    }
}
