package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

@Slf4j(topic = "c.DataContainerStamped")
public class DataContainerStamped {
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data){
        this.data = data;
    }

    public int read(int readTime) throws InterruptedException {
        //乐观读锁
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking ... {}",stamp);
        TimeUnit.SECONDS.sleep(readTime);
        if (lock.validate(stamp)){
            //如果验证戳成功，证明没有被改写过，
            log.debug("read finish ... {}",stamp);
            //返回数据
            return data;
        }
        //否则的话，就证明有过读取，进行锁升级
        log.debug("updating to read lock ...{}",stamp);
        try {
            //加锁
            stamp = lock.readLock();
            log.debug("read lock {}",stamp);
            TimeUnit.SECONDS.sleep(readTime);
            log.debug("read finish ...{}",stamp);
            return data;
        }
        finally {
            log.debug("read unlock {}",stamp);
            //解锁
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData){
        long stamp = lock.writeLock();  //获取戳，加锁
        log.debug("write lock {}",stamp);
        try {
            //模拟写的过程
            TimeUnit.SECONDS.sleep(2);
            this.data = newData;
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            log.debug("write unlock {}",stamp);
            lock.unlockWrite(stamp);
        }
    }
}
