package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuardedSuspension3 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();   //等待收信
        }
        //等待一秒
        TimeUnit.SECONDS.sleep(1);
        //获得信件编号
        for (Integer id: MailBoxes.getIds()){
            new Postman(id,"内容:" + id).start();
        }
    }
}

/**
 * 居民类，消费Mailboxes中的信件
 */
@Slf4j(topic = "c.People")
class People extends Thread{
    @Override
    public void run() {
        //收信
        GuardedObjectV3 guardObject = MailBoxes.createGuardObject();
        log.debug("开始收信 id:{}",guardObject.getId());
        Object mail = guardObject.get(5000);
        log.debug("收到信 id:{},内容:{}",guardObject.getId(),mail);
    }
}

/**
 * 邮递员类，向mailboxes中添加信件
 */
@Slf4j(topic = "c.Postman")
class Postman extends Thread{
    private int mailId; //信箱的ID
    private String mail; //信件内容

    public Postman(int id,String mail){
        this.mailId = id;
        this.mail = mail;
    }


    @Override
    public void run() {
        GuardedObjectV3 guardedObject = MailBoxes.getGuardedObject(mailId);
        log.debug("开始送信:{},内容:{}",mailId,mail);
        guardedObject.complete(mail);
    }
}


/**
 * 存储多个GuardedObject
 */
class MailBoxes{
    //保证线程安全,使用hashtable
    private static Map<Integer,GuardedObjectV3> boxes = new Hashtable<>();
    private static int id = 1;

    private static synchronized int generateId(){
        //产生唯一的ID
        return id ++;
    }

    /**
     * 因为 Hashtable是线程安全的，所以下面两个方法是不需要加synchronized关键字的
     */
    //产生GuardedObject
    public static GuardedObjectV3 createGuardObject(){
        GuardedObjectV3 v3 = new GuardedObjectV3(generateId());
        boxes.put(v3.getId(), v3);
        return v3;
    }

    public static GuardedObjectV3 getGuardedObject(int id){
        return boxes.remove(id);
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
}

class GuardedObjectV3{
    //添加id，标识唯一
    private int id;

    public GuardedObjectV3(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

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
                try{
                    this.wait(waitTime);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object o){
        synchronized (this){
            this.response = o;
            this.notifyAll();
        }
    }
}