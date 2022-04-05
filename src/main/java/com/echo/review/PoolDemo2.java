package com.echo.review;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class PoolDemo2 {
    public static void main(String[] args) {
        SemaphorePool pool = new SemaphorePool(3);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                MockConnection mockConnection = pool.borrow();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.free(mockConnection);
            }).start();
        }
    }
}
class SemaphorePool{
    private int size;
    private MockConnection[] mockConnections;
    private AtomicIntegerArray connectState;
    private Semaphore semaphore;
    public SemaphorePool(int size){
        this.size = size;
        this.mockConnections = new MockConnection[size];
        for (int i = 0; i < size; i++) {
            mockConnections[i] = new MockConnection("小猪佩奇" + i);
        }
        this.connectState = new AtomicIntegerArray(new int[size]);
        this.semaphore = new Semaphore(size);
    }

    public MockConnection borrow(){
        try {
            semaphore.acquire();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        for (int i = 0; i < size; i++) {
            if (connectState.get(i) == 0){
                if (connectState.compareAndSet(i,0,1)){
                    return mockConnections[i];
                }
            }
        }
        //因为信号量和数组的大小一致，所以执行到for循环的地方时，肯定是有线程已经释放了连接
        return null;
    }

    public void free(MockConnection connection){
        for (int i = 0; i < size; i++) {
            if (mockConnections[i] == connection){
                connectState.set(i,0);
                semaphore.release();
                break;
            }
        }
    }
}
