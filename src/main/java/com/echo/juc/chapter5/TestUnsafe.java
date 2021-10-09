package com.echo.juc.chapter5;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class TestUnsafe {
    public static void main(String[] args) throws Exception {
        //获取unsafe对象
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        //1. 如果字段不是静态字段的话,要传入反射类的对象.如果传null是会报java.lang.NullPointerException
        //
        //2. 如果字段是静态字段的话,传入任何对象都是可以的,包括null,这里我们获取的这个字段是静态字段
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe);

        //1.获取属性的偏移地址(这里获取Teacher类中的id和name属性的偏移地址)
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        Teacher t = new Teacher();
        System.out.println("CAS前：" + t);
        //2.执行cas操作
        /**
         * 参数1：要执行cas的对象
         * 参数2：要执行cas对象的域偏移量
         * 参数3：执行cas操作前，该id域的值
         * 参数4：执行cas操作后，要将该id修改的值
         */
        unsafe.compareAndSwapInt(t,idOffset,0,1);

        unsafe.compareAndSwapObject(t,nameOffset,null,"张三");

        System.out.println("CAS后：" + t);
    }
}
class Teacher{
    volatile int id;
    volatile String name;

    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Teacher() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
