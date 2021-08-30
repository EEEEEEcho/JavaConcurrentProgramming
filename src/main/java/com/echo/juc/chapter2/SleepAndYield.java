package com.echo.juc.chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j(topic = "c.SleepAndYield")
public class SleepAndYield {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug("t1 state {}",t1.getState());
        try{
            Thread.sleep(500);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        log.debug("t1 state {}",t1.getState());

//        Set<Integer> hasSet = new HashSet<>();
//        Set<Integer> linkSet = new LinkedHashSet<>();
//        Set<Integer> treeSet = new TreeSet<>();
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Random random = new Random();
//            int val = random.nextInt(100);
//            list.add(val);
//            hasSet.add(val);
//            linkSet.add(val);
//            treeSet.add(val);
//        }
//        System.out.println(list);
//        System.out.println(hasSet);
//        System.out.println(linkSet);
//        System.out.println(treeSet);
//        Map<String,String> map = new HashMap<>();
//        Set<Map.Entry<String, String>> entries = map.entrySet();
//        for (Map.Entry<String, String> entry : entries) {
//            entry.getValue();
//            entry.getKey();
//        }
    }
}

