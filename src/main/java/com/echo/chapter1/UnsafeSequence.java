package com.echo.chapter1;

public class UnsafeSequence {
    private int value;

    /**
     * 返回一个唯一的数值
     * @return
     */
    public int getNext(){
        return value++;
    }

    public static void main(String[] args) {
        UnsafeSequence s = new UnsafeSequence();
        System.out.println(s.getNext());
    }
}
