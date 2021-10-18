package com.echo.juc.chapter7;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TimerDemo")
public class TimerDemo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask timerTask1 = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("task 1");
                TimeUnit.SECONDS.sleep(2);
            }
        };

        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };

        log.debug("start....");
        //延迟1秒后执行timerTask1
        timer.schedule(timerTask1,1000);
        //延迟1秒后执行timerTask2
        timer.schedule(timerTask2,1000);
    }
}
