package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestReadWriteLock {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        new Thread(() -> {
            try {
                dataContainer.write("Hello");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                String read = (String) dataContainer.read();
                System.out.println(read);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                String read = (String) dataContainer.read();
                System.out.println(read);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
class DataContainer{
    private Object data;
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    //读锁
    private ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    //写锁
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public Object read() throws InterruptedException{
        System.out.println("加读锁");
        readLock.lock();
        try {
            System.out.println("读取");
            TimeUnit.SECONDS.sleep(2);
            return data;
        }
        finally {
            System.out.println("解读锁");
            readLock.unlock();
        }
    }
    public void write(Object data) throws Exception{
        System.out.println("加写锁");
        writeLock.lock();
        try {
            System.out.println("写入");
            TimeUnit.SECONDS.sleep(2);
            this.data = data;
        }
        finally {
            System.out.println("解写锁");
            writeLock.unlock();
        }
    }
}
