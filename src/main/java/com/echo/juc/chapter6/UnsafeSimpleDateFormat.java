package com.echo.juc.chapter6;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Slf4j(topic = "c.UnsafeSimpleDateFormat")
public class UnsafeSimpleDateFormat {
    public static void main(String[] args) {
        //test1();
        //JDK8新引入的线程安全的日期格式化对象
        DateTimeFormatter stf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor parse = stf.parse("2008-08-09");
                log.debug("{}", parse);
            }).start();
        }
    }

    public static void test1(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                synchronized (sdf){
                    try {
                        log.debug("{}",sdf.parse("1951-1-04"));
                    }
                    catch (Exception e){
                        log.error("{}",e);
                    }
                }
            }).start();
        }
    }
}
