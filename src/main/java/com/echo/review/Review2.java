package com.echo.review;

import java.util.concurrent.TimeUnit;

public class Review2 {
    static boolean run = true;

    public static void main(String[] args) {
        new Thread(() -> {
            while (run){
                int i = 0;
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        run = false;
    }
}
