package com.echo.review;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(3,3,3,TimeUnit.SECONDS,(((queue, task) -> {
            task.run();
        })));
        for (int i = 0; i < 10; i++) {
            int j = i;
            pool.execute(() -> {
                System.out.println("小猪佩奇:" + j);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
class ThreadPool{
    //核心线程数
    private int coreSize;
    //最大线程数。我没有实现救急线程

    //线程集合
    private HashSet<Worker> workers;

    //阻塞任务队列
    private BlockingQueue<Runnable> taskQueue;

    //等待超时时间
    private long timeout;

    //超时时间单元
    private TimeUnit timeUnit;

    //拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize,int queueSize,
                      long timeout, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.workers = new HashSet<>();
        this.taskQueue = new BlockingQueue<>(queueSize);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable runnable){
        synchronized (workers){
            if(workers.size() < coreSize){
                Worker worker = new Worker(runnable);
                worker.start();
                workers.add(worker);
            }
            else{
                taskQueue.tryPut(runnable,rejectPolicy);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task){
            this.task = task;
        }

        @Override
        public void run() {
            //只要提交的任务不为空，且任务队列里还能掏出来任务，那就不断地拿出来任务执行
            while (task != null || (task = taskQueue.poll(timeout,timeUnit)) != null){
                try{
                    task.run();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    task = null;
                }
            }
            //所有任务都没有了，移除该工作线程
            synchronized (workers){
                workers.remove(this);
            }
        }
    }
}
class BlockingQueue<T>{
    //队列大小
    private int size;
    //双端队列
    private ArrayDeque<T> deque = new ArrayDeque<>();
    //锁
    private ReentrantLock lock = new ReentrantLock();
    //条件变量
    private Condition empty = lock.newCondition();
    private Condition full = lock.newCondition();

    public BlockingQueue(int size){
        this.size = size;
    }
    //取任务有很多种方式。
    //1.阻塞式
    public T poll(){
        lock.lock();
        try {
            //如果队列为空，就一直等待
            while (deque.isEmpty()){
                try {
                    empty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = deque.removeFirst();
            //通知生产者
            full.signalAll();
            return t;
        }
        finally {
            lock.unlock();
        }
    }
    //2.超时等待
    public T poll(long timeout,TimeUnit timeUnit){
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (deque.isEmpty()){
                if (nanos <= 0){
                    //超时了 返回Null
                    return null;
                }
                try {
                    nanos = empty.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = deque.removeFirst();
            full.signal();
            return t;
        }
        finally {
            lock.unlock();
        }
    }
    //放任务也有两种方式，一种是阻塞时的添加，另一种是带有拒绝策略的添加
    //1.阻塞式的添加
    public void put(T t){
        lock.lock();
        try {
            while (deque.size() == size){
                try {
                    full.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            deque.addLast(t);
            empty.signalAll();
        }
        finally {
            lock.unlock();
        }
    }
    //2.带有拒绝策略的添加
    public void tryPut(T t,RejectPolicy<T> rejectPolicy){
        lock.lock();
        try {
            if (deque.size() == size){
                rejectPolicy.reject(this,t);
            }
            else{
                deque.addLast(t);
                empty.signalAll();
            }
        }
        finally {
            lock.unlock();
        }
    }
}
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}