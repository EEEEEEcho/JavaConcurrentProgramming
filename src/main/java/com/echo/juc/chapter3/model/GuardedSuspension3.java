package com.echo.juc.chapter3.model;

public class GuardedSuspension3 {
}

class GuardedObjectV3{
    //添加id，标识唯一
    private int id;


    private Object response;

    public Object get(long timeout){
        synchronized (this){
            //开始时间
            long begin = System.currentTimeMillis();
            //经历的时间
            long passedTime = 0;
            while (response == null){
                //这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                //经历的时间超过了最大等待时间，退出循环
                if (waitTime < 0){
                    break;
                }

            }
            return response;
        }
    }
}