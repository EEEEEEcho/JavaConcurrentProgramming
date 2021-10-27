package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j(topic = "c.TestReadWriteLock")
public class TestReadWriteLock {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        new Thread(() -> {
            try {
                dataContainer.read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();

        new Thread(() -> {
            dataContainer.write();
        }, "t2").start();
    }
}

@Slf4j(topic = "c.DataContainer")
class DataContainer {
    //要保护的共享数据
    private Object data;
    //读写锁
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    //读锁变量
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    //写锁变量
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() throws InterruptedException {
        log.debug("获取读锁。。");
        //加锁
        r.lock();
        try {
            log.debug("读取");
            TimeUnit.SECONDS.sleep(2);
            return data;
        } finally {
            //释放锁
            log.debug("释放读锁..");
            r.unlock();
        }
    }

    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try {
            log.debug("写入");
        } finally {
            log.debug("释放写锁..");
            w.unlock();
        }
    }
}


