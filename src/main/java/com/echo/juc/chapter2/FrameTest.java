package com.echo.juc.chapter2;

public class FrameTest {
    public static void main(String[] args) {
        method1(10);
    }
    private static void method1(int x){
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2(){
        return new Object();
    }
}
