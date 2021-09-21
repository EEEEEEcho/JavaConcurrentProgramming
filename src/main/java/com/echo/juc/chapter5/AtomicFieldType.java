package com.echo.juc.chapter5;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicFieldType {
    public static void main(String[] args) {
        Student s = new Student();
        //为Student对象中的name字段创建一个属性更新器。其中name字段是String类型的
        AtomicReferenceFieldUpdater<Student, String> updater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class,String.class,"name");
        //如果期望的是null，就是假如原来的是null,就更新为Echo
        updater.compareAndSet(s,null,"Echo");

        System.out.println(s);

    }
}
class Student{
    volatile String name;    //不能用private，因为字段更新器要访问该字段,必须要用volatile修饰

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
