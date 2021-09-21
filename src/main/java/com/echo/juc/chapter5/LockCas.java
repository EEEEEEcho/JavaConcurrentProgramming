package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS锁
 */
@Slf4j(topic = "c.LockCas")
public class LockCas {
    //0，没加锁
    //1，加锁
    private AtomicInteger state = new AtomicInteger(0);

    public void lock(){
        while (true){
            if (state.compareAndSet(0,1)){
                break;
            }
        }
    }

    public void unlock(){
        log.debug("unlock...");
        state.set(0);
    }

    public static void main(String[] args) {
        LockCas lockCas = new LockCas();
        new Thread(() -> {
            log.debug("begin....");
            lockCas.lock();
            try {
                log.debug("lock...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            finally {
                lockCas.unlock();
            }
        }).start();

        new Thread(() -> {
            log.debug("begin....");
            lockCas.lock();
            try {
                log.debug("lock...");
            }
            finally {
                lockCas.unlock();
            }
        }).start();
    }
}
