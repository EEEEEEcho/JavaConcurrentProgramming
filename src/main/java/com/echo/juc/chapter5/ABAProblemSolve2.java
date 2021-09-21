package com.echo.juc.chapter5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

@Slf4j(topic = "c.ABAProblemSolve2")
public class ABAProblemSolve2 {
    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        //参数2 mark 表示垃圾袋装满了
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag,true);
        log.debug("start...");
        GarbageBag pre = ref.getReference();
        log.debug(pre.toString());

        //保洁阿姨替换
        new Thread(()->{
            log.debug("保洁阿姨start...");
            bag.setDesc("空垃圾袋");
            ref.compareAndSet(bag,bag,true,false);
            log.debug(bag.toString());
        },"保洁阿姨").start();

        TimeUnit.SECONDS.sleep(1);
        log.debug("想换一只新的垃圾袋？");
        //期望的标记exceptMark为true,如果原来的exceptedMark是true，才把newMark变为false
        boolean success = ref.compareAndSet(pre, new GarbageBag("空垃圾袋"), true, false);
        log.debug("换了吗 : {}",success);
        log.debug(ref.getReference().toString());
    }
}
class GarbageBag {
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "GarbageNag{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
