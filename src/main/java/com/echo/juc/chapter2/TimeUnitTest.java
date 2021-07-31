package com.echo.juc.chapter2;

import java.util.concurrent.TimeUnit;

public class TimeUnitTest {
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1); //睡眠一毫秒
        TimeUnit.SECONDS.sleep(1);  //睡眠一秒
        TimeUnit.HOURS.sleep(1);    //睡眠一小时
    }
}
