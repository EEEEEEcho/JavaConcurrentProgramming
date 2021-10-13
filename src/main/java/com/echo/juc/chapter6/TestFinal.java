package com.echo.juc.chapter6;

class FinalDemo{
    static int A = 10;
    static int B = Short.MAX_VALUE + 1;
}

public class TestFinal {
    public static void main(String[] args) {
        System.out.println(FinalDemo.A);
        System.out.println(FinalDemo.B);
    }
}

