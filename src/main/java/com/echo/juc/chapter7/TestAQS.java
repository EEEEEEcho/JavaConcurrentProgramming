package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
@Slf4j(topic = "c.TestAQS")
public class TestAQS {
    public static void main(String[] args) {
        MyLock myLock = new MyLock();
        new Thread(()->{
            myLock.lock();
            log.debug("locking...");
            myLock.lock();
            log.debug("locking...");
            try {
                log.debug("locking...");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking...");
                myLock.unlock();
            }
        },"t1").start();

        new Thread(()->{
            myLock.lock();
            try {
                log.debug("locking...");
            }
            finally {
                log.debug("unlocking...");
                myLock.unlock();
            }
        },"t2").start();
    }
}

/**
 * 自定义实现不可重入锁
 */
class MyLock implements Lock{

    /**
     * 同步器类，自定义的不可重入锁的大部分功能是由同步器类实现的
     * 实现独占锁
     */
    class MySync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            //return super.tryAcquire(arg);
            //将state从0尝试改为1
            if(compareAndSetState(0,1)){
                //如果成功了，表示加锁成功,并设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //return super.tryRelease(arg);
            setExclusiveOwnerThread(null);  //表示没有线程可以占用
            setState(0);    //解锁
            //这样放置的顺序是因为state是volatile的，开启一个写屏障，防止指令重排序
            return true;
        }

        @Override   //是否持有独占锁
        protected boolean isHeldExclusively() {
            //return super.isHeldExclusively();
            return getState() == 1; //看state是否为1
        }

        public Condition newCondition(){
            return new ConditionObject();
        }
    }
    private MySync sync = new MySync();

    @Override   //加锁，不成功会进入等待队列
    public void lock() {
        sync.acquire(1);    //内部会调用tryAcquire
    }

    @Override   //加锁，可打断式的加锁
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override   //尝试加锁，尝试加锁一次，如果不成功返回false
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override   //尝试加锁，带超时时间
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override   //解锁
    public void unlock() {
        sync.release(0);
    }

    @Override   //创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}