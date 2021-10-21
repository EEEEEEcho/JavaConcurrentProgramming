package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo2 {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        Integer result = pool.invoke(new AddTask(1, 5));
        System.out.println(result);
    }
}

@Slf4j(topic = "c.AddTask")
class AddTask extends RecursiveTask<Integer>{
    int begin;
    int end;

    public AddTask(int begin,int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "AddTask{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }


    @Override
    protected Integer compute() {
        //假如遇到5，5的情况
        if(begin == end){
            log.debug("join() {}",begin);
            return begin;
        }
        if(end - begin == 1){
            log.debug("join() {} + {} = {}",begin,end,begin + end);
            return begin + end;
        }

        //二分式任务分解
        int mid = (end + begin) / 2;

        AddTask t1 = new AddTask(begin, mid);
        t1.fork();
        AddTask t2 = new AddTask(mid + 1, end);
        t2.fork();
        log.debug("fork() {} + {} = ?",t1,t2);

        int result = t1.join() + t2.join();
        log.debug("join() {} + {} = {}",t1,t2,result);
        return result;
    }
}