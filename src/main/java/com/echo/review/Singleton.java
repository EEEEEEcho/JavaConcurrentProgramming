package com.echo.review;

import java.io.Serializable;

public final class Singleton implements Serializable {
    private static volatile Singleton INSTANCE = null;
    private Singleton(){
    }
    public static Singleton getInstance(){
        if (INSTANCE ==null){
            synchronized (Singleton.class){
                if (INSTANCE == null){
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
    public Object readResolve() {
        return INSTANCE;
    }
}
