package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TestAQS {
<<<<<<< HEAD
}
class MyLock implements Lock{
    private MySync sync = new MySync();

=======

}
class MyLock implements Lock{
    private MySync sync = new MySync();
>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
<<<<<<< HEAD
        return sync.tryAcquireNanos(1, unit.toNanos(time));
=======
        return sync.tryAcquireNanos(1,unit.toNanos(time));
>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
    }

    @Override
    public void unlock() {
        sync.release(0);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    class MySync extends AbstractQueuedSynchronizer{
<<<<<<< HEAD
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0,1)){
=======

        @Override
        protected boolean tryAcquire(int arg) {
            //cas设置独占锁
            if (compareAndSetState(0,arg)){
>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
<<<<<<< HEAD
            return getState() == 1;
=======
            return getState() == 1; //看state是否为1
>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
        }

        public Condition newCondition(){
            return new ConditionObject();
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> a98f59ae65516d65060f60eff5139e7e98809d18
