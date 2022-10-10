//package com.echo.review;
//
//<<<<<<< HEAD
//import java.util.concurrent.atomic.LongAdder;
//
//public final class Singleton {
//    private static volatile Singleton INSTANCE = null;
//    private Singleton(){
//
//    }
//    public static Singleton getInstance(){
//        if(INSTANCE == null){
//=======
//import java.io.Serializable;
//
//public final class Singleton implements Serializable {
//    private static volatile Singleton INSTANCE = null;
//    private Singleton(){
//    }
//    public static Singleton getInstance(){
//        if (INSTANCE ==null){
//>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
//            synchronized (Singleton.class){
//                if (INSTANCE == null){
//                    INSTANCE = new Singleton();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//<<<<<<< HEAD
//
//    public static void main(String[] args) {
//
//=======
//    public Object readResolve() {
//        return INSTANCE;
//>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
//    }
//}
