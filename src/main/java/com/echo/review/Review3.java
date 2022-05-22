package com.echo.review;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Review3 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);

        Unsafe unsafe = (Unsafe)theUnsafe.get(null);
        System.out.println(unsafe);
        long ageOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("age"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));
        Teacher teacher = new Teacher();
        System.out.println(teacher);
        boolean b = unsafe.compareAndSwapInt(teacher, ageOffset, 0, 1);
        boolean echo = unsafe.compareAndSwapObject(teacher, nameOffset, null, "Echo");
        System.out.println(teacher);

    }
}
class Teacher{
    volatile String name;
    volatile int age;
    public Teacher(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}