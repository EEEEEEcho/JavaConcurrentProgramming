package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
@Slf4j(topic = "c.TestWordCount")
public class TestWordCount {
    public static void main(String[] args) {
        //test1();
        //使用普通Map
//        test1(
//                ()->new HashMap<String,Integer>(),  //Supplier
//
//                (map,words)->{
//                    for (String word: words){           //Consumer
//                        Integer counter = map.get(word);
//                        int newValue = counter == null? 1 : counter + 1;
//                        map.put(word,newValue);
//                    }
//                }
//                );
        //错误使用ConcurrentHashMap
        test1(
                ()->new ConcurrentHashMap<String,Integer>(),  //Supplier

                (map,words)->{
                    for (String word: words){           //Consumer
                        Integer counter = map.get(word);
                        int newValue = counter == null? 1 : counter + 1;
                        map.put(word,newValue);
                    }
                }
        );
        //使用computeIfAbsent和累加器进行改造
        test1(
                //这里提供的值不再是单纯的值，而是一个累加器。一个key对应一个累加器。
                //如果是该值，则累加器自增
                ()->new ConcurrentHashMap<String, LongAdder>(),
                (map,words)->{
                    for (String word: words){           //Consumer
                        //如果没有这个key，那么就计算生成一个value，然后将key-value放入map
                        LongAdder longAdder = map.computeIfAbsent(word, (key) -> new LongAdder());
                        longAdder.increment();
                    }
                }
        );
    }
    public static<V> void test1(Supplier<Map<String,V>> supplier,   //提供保存结果的集合
                                BiConsumer<Map<String,V>, List<String>> consumer){  //消费器，一个是集合，一个是结果
        //从26个文件中收集所有的字母，并统计其出现的次数，如果正常，结果应该是每个字母200次
        Map<String, V> counterMap = supplier.get();
        List<Thread> ts = new ArrayList<>();
        //开启26个线程，去从文件中读取字母，并计数
        for (int i = 1; i <= 26 ; i++) {
            int idx = i;
            Thread thread = new Thread(()->{
                List<String> words = readFromFile(idx);
                consumer.accept(counterMap,words);
            });
            ts.add(thread);
        }
        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            try {
                t.join();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        System.out.println(counterMap);
    }
    public static List<String> readFromFile(int i) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("tmp/" + i + ".txt")))) {
            while (true) {
                String word = in.readLine();
                if (word == null) {
                    break;
                }
                words.add(word);
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
