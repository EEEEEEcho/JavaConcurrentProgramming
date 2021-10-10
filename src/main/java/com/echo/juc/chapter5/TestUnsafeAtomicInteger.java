package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

@Slf4j(topic = "c.TestUnsafeAtomicInteger")
public class TestUnsafeAtomicInteger {
    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(10000));
    }
}
class MyAtomicInteger implements Account{
    //被保护的整型变量
    private volatile int value;
    //被保护的变量的偏移量
    private static final long valueOffset;
    //UNSAFE对象
    static final Unsafe UNSAFE;
    static {
        //初始化UNSAFE对象
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            //使用UNSAFE对象获取到MyAtomicInteger中value域的偏移值
            valueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void decrement(int amount){
        while (true){
            int prev = this.value;
            int next = this.value - amount;
            //使用UNSAFE的cas方法进行cas操作
            if (UNSAFE.compareAndSwapInt(this,valueOffset,prev,next)) {
                break;
            }
        }
    }

    @Override
    public Integer getBalance() {
        return this.getValue();
    }

    @Override
    public void withDraw(Integer amount) {
        this.decrement(amount);
    }
}

