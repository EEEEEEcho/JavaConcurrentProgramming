package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "c.TestAccount")
public class TestAccount {
    public static void main(String[] args) {
//        AccountUnsafe account = new AccountUnsafe(10000);
//        Account.demo(account);

        AccountCas accountCas = new AccountCas(10000);
        Account.demo(accountCas);
    }
}

class AccountCas implements Account{
    private AtomicInteger balance;

    public AccountCas(int balance) {
        this.balance = new AtomicInteger(balance);
    }


    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withDraw(Integer amount) {
        while (true){
            //获取最新的余额值
            int prev = balance.get();
            //要修改的值
            int next = prev - amount;
            //设置新值
            if (balance.compareAndSet(prev,next)){
                //如果修改成功，跳出循环
                break;
            }
        }
    }
}
class AccountUnsafe implements Account{
    private Integer balance;

    public AccountUnsafe(Integer balance){
        this.balance = balance;
    }


    @Override
    public Integer getBalance() {
        synchronized (this){
            return this.balance;
        }
    }

    @Override
    public void withDraw(Integer amount) {
        synchronized (this){
            this.balance -= amount;
        }

    }
}
interface Account{
    //获取余额
    Integer getBalance();

    //取款
    void withDraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account){
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1; i++) {
            ts.add(new Thread(() -> {
                account.withDraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance() + " cost : " + (end-start)/1000_000 + "ms");
    }
}

