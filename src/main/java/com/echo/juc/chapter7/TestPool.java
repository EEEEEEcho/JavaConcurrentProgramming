package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 */
@Slf4j(topic = "c.TestPool")
public class TestPool {
    public static void main(String[] args) {
        //两个线程的池
        ThreadPool pool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
        for (int i = 0; i < 5; i++) {
            int j = i;
            pool.execute(() -> {
                log.debug("{}",j);
            });
        }
    }
}
@Slf4j(topic = "c.ThreadPool")
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;

    //线程集合
    private HashSet<Worker> workers = new HashSet();

    //核心线程数
    private int coreSize;

    //获取任务的超时时间
    private long timeout;

    private TimeUnit timeUnit;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit,int queueSize) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueSize);
    }

    //执行任务
    public void execute(Runnable task){
        //保证workers的线程安全，因为workers是多个线程可能会操作的
        synchronized (workers){
            //当任务的数量没有超过coreSize时，直接交给worker对象执行
            if (workers.size() < coreSize){
                Worker worker = new Worker(task);
                log.debug("新增 worker{}执行{}",worker,task);
                //加入线程集合
                workers.add(worker);
                //执行任务
                worker.start();
            }
            else{
                //当任务数量超过coreSize时，将任务加入队列暂存
                taskQueue.put(task);
                log.debug("加入任务队列 {}",task);
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
            //执行任务
            //1.当task不为空，直接执行任务
            //2.当task执行完毕，再接着从任务队列获取任务执行。
            //while (task != null || (task = taskQueue.get()) != null){   //无超时限制，线程不会终止
            while (task != null || (task = taskQueue.poll(timeout,timeUnit)) != null){ //有超时时间，线程会终止
                try {
                    log.debug("正在执行 {}",task);
                    task.run();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    //无论任务是否正常执行完毕，在结束后，将task置为null
                    task = null;
                }
            }
            //传过来的任务执行完了，线程池中也没有线程了，从线程集合中移除该线程
            synchronized (workers){
                log.debug("worker 被移除:{}",this);
                workers.remove(this);
            }
        }
    }


}

/**
 * 任务队列
 * */
@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T>{
    //1.任务队列容器
    private Deque<T> queue = new ArrayDeque<>();

    //2.锁，用来防止线程竞争向队列中添加或取出任务
    private ReentrantLock lock = new ReentrantLock();

    //3.生产者条件变量，如果队列已满，新来的生产者进入该队列等待
    private Condition fullWaitSet = lock.newCondition();

    //4.消费者条件变量，如果队列已空，要进行消费的消费者进入该队列等待
    private Condition emptyWaitSet = lock.newCondition();

    //5.队列容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    //获取，如果超时则放弃
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try{
            //将timeout统一转换为纳秒
            long nanos = unit.toNanos(timeout);

            while (queue.isEmpty()){
                //队列为空，进入等待队列
                try {
                    if (nanos <= 0){
                        return null;
                    }
                    //返回的是，等完之后剩余的时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            //唤醒另一个队列中的生产者
            fullWaitSet.signal();
            return t;
        }
        finally {
            lock.unlock();
        }
    }

    //获取
    public T get(){
        lock.lock();
        try{
            while (queue.isEmpty()){
                //队列为空，进入等待队列
                try {
                    emptyWaitSet.await();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            //唤醒另一个队列中的生产者
            fullWaitSet.signal();
            return t;
        }
        finally {
            lock.unlock();
        }
    }

    //添加
    public void put(T e){
        lock.lock();
        try {
            while (queue.size() == capacity){
                //如果满了，进入等待队列
                try{
                    fullWaitSet.await();
                }
                catch (InterruptedException x){
                    x.printStackTrace();
                }
            }
            queue.addLast(e);
            //添加完新元素之后，唤醒 另一个等待队列中的消费者
            emptyWaitSet.signal();
        }
        finally {
            lock.unlock();
        }
    }

    //获取大小
    public int size(){
        lock.lock();
        try {
            return queue.size();
        }
        finally {
            lock.unlock();
        }
    }
}