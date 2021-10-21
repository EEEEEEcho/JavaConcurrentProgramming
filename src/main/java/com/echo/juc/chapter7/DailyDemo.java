package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.DailyDemo")
public class DailyDemo {

    //如何让每周四18：00：00定时执行任务
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //获取周四时间(修改当前时间)
        LocalDateTime targetTime = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        System.out.println(targetTime);
        //如果当前时间大于本周周四（例如今天周五）,必须找到下周周四
        if (now.compareTo(targetTime) > 0){
            //在本周周四基础上，加一周，变成下周周四
            targetTime = targetTime.plusWeeks(1);
        }

        long initialDelay = Duration.between(now, targetTime).toMillis();

        long period = 60 * 60 * 24 * 7;

        //initailDelay表示当前时间和周四的时间差
        //period一周的间隔时间
        pool.scheduleAtFixedRate(()->{
            log.debug("Hello world");
        },initialDelay,period, TimeUnit.SECONDS);
    }
}


