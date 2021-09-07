package com.echo.juc.chapter3.model;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ProducerAndConsumer")
public class ProducerAndConsumer {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                messageQueue.put(new Message(id,"值" + id));
            },"生产者" + i).start();
        }
        new Thread(() -> {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(1);
                    Message take = messageQueue.take();
                    System.out.println(take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者").start();
    }
}

/**
 * 消息队列类，该消息队列是Java线程之间通信的
 * 而不是进程之间通信的
 */
@Slf4j(topic = "c.MessageQueue")
class MessageQueue{
    //消息队列集合
    private LinkedList<Message> list = new LinkedList<>();
    //消息队列容量
    private int capcity;

    public MessageQueue(int capcity){
        this.capcity = capcity;
    }

    //获取消息
    public Message take(){
        //检查队列是否为空
        synchronized (list){
            while (list.isEmpty()){
                try {
                    log.debug("队列为空，消费者线程等待");
                    list.wait();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            //从队列头部获取消息返回
            Message message = list.removeFirst();
            log.debug("已消费消息:{}",message);
            //通知添加消息的进程已经添加了
            list.notifyAll();
            //消息返回
            return message;
        }

    }

    //存入消息
    public void put(Message message){
        synchronized (list){
            while (list.size() == capcity){
                try {
                    log.debug("队列已满，生产者线程阻塞");
                    list.wait();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            //将消息加入队列尾部
            log.debug("已生产消息:{}",message);
            list.add(message);
            //唤醒等待消息的线程
            list.notifyAll();
        }
    }
}

final class Message{
    private int id;
    private Object value;

    public Message(int id,Object value){
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}