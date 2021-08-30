package com.echo.juc.chapter2;

public class SleepDemo {
    public static void main(String[] args) {
        int i = 0;
        while (true){
            try {
                i++;
                i--;
                Thread.sleep(50);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
