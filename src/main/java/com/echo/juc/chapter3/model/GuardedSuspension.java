package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.GuardedSuspension")
public class GuardedSuspension {
    //线程一等待线程二的下载结果
    public static void main(String[] args) {
        GuardObject guardObject = new GuardObject();
        new Thread(() -> {
            //等待结果
            log.debug("等待结果");
            String result = (String) guardObject.get();
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
 */
class GuardObject{
    private Object response;    //结果

    //获取结果
    public Object get(){
        synchronized (this){
            while (response == null){   //还没有结果
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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