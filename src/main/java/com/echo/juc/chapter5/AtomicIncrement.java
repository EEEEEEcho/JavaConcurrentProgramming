package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j(topic = "c.AtomicIncrement")
public class AtomicIncrement {


    public static void main(String[] args) {
        //比较 AtomicLong 与 LongAdder
        demo(
                () -> new AtomicLong(0),
                (adder) -> adder.getAndIncrement()
                );
        demo(
                () -> new LongAdder(),
                (adder) -> adder.increment()
        );
    }
    /**
     * 性能测试的方法
     * @param adderSupplier 被测试的累加器
     * @param action 累加器的行为
     * @param <T> 累加器泛型
     */
    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action) {
        T adder = adderSupplier.get();
        long start = System.nanoTime();
        List<Thread> ts = new ArrayList<>();
        // 4 个线程，每人累加 50 万,共累加到200w
        for (int i = 0; i < 40; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
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
        System.out.println(adder + " cost:" + (end - start)/1000_000);
    }
}
