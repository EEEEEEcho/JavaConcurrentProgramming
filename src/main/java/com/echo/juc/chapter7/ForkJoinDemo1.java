package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@Slf4j(topic = "c.ForkJoinDemo1")
public class ForkJoinDemo1 {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        //求1~5累加的和
        Integer sum = forkJoinPool.invoke(new MyTask(5));
        System.out.println(sum);

        //拆分 ：
        /**
         * new MyTask(5) = 5 + new MyTask(4){4 + new MyTask(3){3 + new MyTask(2){
         *      2 + new MyTask(1){1}}}}
         */
    }
}

/**
 * 计算1~n之间整数的和
 */
@Slf4j(topic = "c.MyTask")
class MyTask extends RecursiveTask<Integer>{

    private int n;

    public MyTask(int n){
        this.n = n;
    }

    @Override
    public String toString() {
        return "MyTask{" +
                "n=" + n +
                '}';
    }

    @Override
    protected Integer compute() {
//        if (n == 1){
//            return 1;   //终止条件
//        }
//        //拆分任务
//        MyTask t1 = new MyTask(n - 1);
//        //让线程执行
//        t1.fork();
//
//
//        //获取任务结果
//        int result = n + t1.join();
//
//        //返回结果
//        return result;


        // 如果 n 已经为 1,可以求得结果了
        if (n == 1) {
            log.debug("join() {}", n);
            return n;
        }
        // 将任务进行拆分(fork)
        MyTask t1 = new MyTask(n - 1);
        t1.fork();
        log.debug("fork() {} + {}", n, t1);

        // 合并(join)结果
        int result = n + t1.join();
        log.debug("join() {} + {} = {}", n, t1, result);
        return result;
    }
}