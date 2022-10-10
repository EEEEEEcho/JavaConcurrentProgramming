//package com.echo.review;
//
//import java.util.ArrayDeque;
//import java.util.Deque;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class TestPool {
//}
//class Pool{
//    //任务队列
//    private BlockingQueue<Runnable> taskList;
//    //任务队列大小
//    private int listSize;
//    //线程集合
//    private HashSet<Worker> workers = new HashSet<>();
//    //核心线程数
//    private int coreSize;
//    //时间单元
//    private TimeUnit timeUnit;
//    //超时时间
//    private int timeout;
//
//    public Pool(int coreSize,TimeUnit timeUnit,int timeout,int listSize){
//        this.coreSize = coreSize;
//        this.timeUnit = timeUnit;
//        this.timeout = timeout;
//        this.taskList = new BlockingQueue<>(listSize);
//    }
//
//    public void execute(Runnable task){
//        synchronized (workers){
//            if(workers.size() < coreSize){
//                Worker worker = new Worker(task);
//                worker.start();
//                workers.add(worker);
//            }
//            else{
//                taskList.put(task);
//            }
//        }
//    }
//
//    //线程
//    class Worker extends Thread{
//        private Runnable task;
//        public Worker(Runnable task){
//            this.task = task;
//        }
//
//        @Override
//        public void run() {
//            while (task != null && (task = taskList.get()) != null){
//                try {
//                    task.run();
//                } finally {
//                    task = null;
//                }
//            }
//            synchronized (workers){
//                workers.remove(this);
//            }
//        }
//    }
//}
//class BlockingQueue<T>{
//    private Deque<T> queue = new ArrayDeque<>();
//    private ReentrantLock lock = new ReentrantLock();
//    private Condition full = lock.newCondition();
//    private Condition empty = lock.newCondition();
//    private int size;
//    public BlockingQueue(int size){
//        this.size = size;
//    }
//
//    public T get(){
//        lock.lock();
//        try{
//            while (queue.isEmpty()){
//                try {
//                    empty.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            T t = queue.removeFirst();
//            full.signal();
//            return t;
//        }
//        finally {
//            lock.unlock();
//        }
//    }
//
//    public void put(T t){
//        lock.lock();
//        try {
//            while (queue.size() == size){
//                try {
//                    full.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            queue.addLast(t);
//            empty.signal();
//        }
//        finally {
//            lock.unlock();
//        }
//    }
//
//    public int getSize(){
//        lock.lock();
//        try {
//            return queue.size();
//        }
//        finally {
//            lock.unlock();
//        }
//    }
//}
