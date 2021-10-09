package com.echo.juc.chapter5;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeAccessor {
    private static final Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Error(e);
        }
    }
    public static Unsafe getUnsafe(){
        return unsafe;
    }
}
