package com.echo.review;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ConnectionPoolDemo {
    public static void main(String[] args) {
        Pool pool = new Pool(3);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                MockConnection mockConnection = pool.borrowConnection();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.freeConnection(mockConnection);
            }).start();
        }
    }
}

class Pool{
    private int size;   //池大小
    private MockConnection[] mockConnections;   //连接数组
    private AtomicIntegerArray borrowState;
    public Pool(int size){
        this.size = size;
        this.mockConnections = new MockConnection[size];
        for (int i = 0; i < size; i++) {
            mockConnections[i] = new MockConnection("小猪佩奇" + i);
        }
        borrowState = new AtomicIntegerArray(new int[this.size]);
    }

    public MockConnection borrowConnection(){
        while (true){
            for (int i = 0; i < size; i++) {
                if (borrowState.get(i) == 0){
                    if (borrowState.compareAndSet(i,0,1)){
                        System.out.println("借到连接 ： " + mockConnections[i]);
                        return mockConnections[i];
                    }
                }
            }
            synchronized (this){
                try {
                    System.out.println("wait....");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void freeConnection(MockConnection mockConnection){
        for (int i = 0; i < size; i++) {
            if (mockConnection == mockConnections[i]){
                borrowState.set(i,0);
                break;
            }
        }
        synchronized (this){
            System.out.println("Free : " + mockConnection);
            //唤醒等待队列中的线程
            this.notifyAll();
        }
    }
}

class MockConnection {
    private String name;
    public MockConnection(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "MockConnection{" +
                "name='" + name + '\'' +
                '}';
    }
}