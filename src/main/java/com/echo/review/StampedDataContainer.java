package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class StampedDataContainer {
    private int data;
    private final StampedLock lock = new StampedLock();

    public StampedDataContainer(int data){
        this.data = data;
    }

    public int read(int readTime) throws Exception{
        long stamp = lock.tryOptimisticRead();
        System.out.println("乐观读锁的stamp:" + stamp);
        TimeUnit.SECONDS.sleep(readTime);
        if (lock.validate(stamp)){
            //验证成功 相当于没有修改，直接返回
            System.out.println("finish read");
            return data;
        }
        //否则的话 证明已经被读取过，进行锁升级
        else{
            stamp = lock.readLock();
            try{
                System.out.println("read lock : " + stamp);
                TimeUnit.SECONDS.sleep(readTime);
                System.out.println("read finish : " + stamp);
                return data;
            }
            finally {
                lock.unlockRead(stamp);
            }
        }
    }

    public void write(int data) throws Exception{
        long stamp = lock.writeLock();
        try{
            TimeUnit.SECONDS.sleep(3);
            this.data = data;
        }
        finally {
            lock.unlockWrite(stamp);
        }
    }

}
