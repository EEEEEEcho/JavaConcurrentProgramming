package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j(topic = "c.GuardedSuspension2")
public class GuardedSuspension2 {


    public static void main(String[] args) {
        GuardObject2 guardObject = new GuardObject2();
        new Thread(() -> {
            //等待结果
            log.debug("等待结果");
            String result = (String) guardObject.get(3000);
            log.debug("获取到了下载结果:{}",result);
        },"线程一").start();

        new Thread(() -> {
            log.debug("执行下载");
            try {
                //等待两秒
                TimeUnit.SECONDS.sleep(2);
                //构造下载结果
                String downLoad = "事情为什么总是向着不好的地方发展。";
                //将下载结果传递
                guardObject.complete(downLoad);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"线程二").start();
    }
}
/**
 * 桥梁，用来线程间消息的传递
 * 超时增强
 */
class GuardObject2{
    private Object response;    //结果

    //获取结果
    //timeout:超时时间
    public Object get(long timeout){
        synchronized (this){
            //记录一个开始时间 15:00:00
            long begin = System.currentTimeMillis();
            //记录一个经历的时间
            long passedTime = 0;
            while (response == null){   //还没有结果
                if (passedTime >= timeout){
                    //经历的时间，超过了所传过来的最大超时时间，就退出循环
                    break;
                }
                try {
                    //this.wait(timeout); // 万一出现虚假唤醒，被一个notifyAll唤醒了15：00：01，那么
                                        //就会继续向下执行，求得passedTime肯定会小于timeout,然后
                                        //从while循环重新开始执行，response肯定为null，且passedTime也
                                        //小于timeout,那么wait的时候，又会重新wait一个完整的timeout，但
                                        //事实上，已经过了一秒了，所以要修改
                    this.wait(timeout - passedTime);    //这样，就算被虚假唤醒了，下次等待的时间也正常了
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //求得经历的时间
                passedTime = System.currentTimeMillis() - begin;    //15:00:02
            }
            return response;
        }
    }

    //产生结果，给response赋值
    public void complete(Object response){
        synchronized (this){
            //给结果成员变量赋值
            this.response = response;
            //通知结果产生了
            this.notifyAll();
        }
    }
}