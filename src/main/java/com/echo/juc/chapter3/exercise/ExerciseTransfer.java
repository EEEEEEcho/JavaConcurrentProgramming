package com.echo.juc.chapter3.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j(topic = "c.ExerciseTransfer")
public class ExerciseTransfer {
    //random为线程安全
    static Random random = new Random();
    public static int randomAccount(){
        return random.nextInt(100) + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b,randomAccount());
            }
        },"t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a,randomAccount());
            }
        },"t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("total: {}",(a.getMoney() + b.getMoney()));
    }

}

class Account{
    private int money;
    private static final Object lock = new Object();

    public Account(int money){
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    //转账
    public void transfer(Account another,int amount){
        synchronized (lock){
//        synchronized (Account.class){
            if (this.money >= amount){
                this.setMoney(this.money - amount);
                another.setMoney(another.getMoney() + amount);
            }
        }
    }
}
