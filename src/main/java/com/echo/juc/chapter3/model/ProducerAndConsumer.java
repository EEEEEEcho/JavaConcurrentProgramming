package com.echo.juc.chapter3.model;

import java.util.LinkedList;

public class ProducerAndConsumer {
}

/**
 * 消息队列类，该消息队列是Java线程之间通信的
 * 而不是进程之间通信的
 */
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
                    list.wait();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            //从队列头部获取消息返回
            Message message = list.removeFirst();
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
                    list.wait();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            //将消息加入队列尾部
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