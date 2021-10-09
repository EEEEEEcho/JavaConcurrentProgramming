package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

@Slf4j(topic = "c.TestUnsafeAtomicInteger")
public class TestUnsafeAtomicInteger {
    public static void main(String[] args) {

    }
}
class MyAtomicInteger{
    //
    private volatile int value;
    static final Unsafe UNSAFE;
    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
    }
}

