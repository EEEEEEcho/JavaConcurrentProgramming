package com.echo.juc.chapter3;

public class MyBenchMark {
    static int x = 0;
    public void a() throws Exception{
        x ++;
    }

    public void b() throws Exception{
        Object o = new Object();
        synchronized (o){
            x ++;
        }
    }
}
