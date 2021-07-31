package com.echo.juc.chapter2;

public class SleepDemo {
    public static void main(String[] args) {
        while (true){
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
