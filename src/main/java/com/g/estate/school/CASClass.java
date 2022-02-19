package com.g.estate.school;

import java.util.stream.IntStream;

/**
 * CAS: compare and swap
 * 是一个无锁操作，乐观锁策略，不会阻塞其他线程操作。他的实现需要硬件指令集的支撑，在JDK1.5后才可以实现。
 * CAS(value , old, new )
 * value: 内存地址实际值
 * old： 旧值
 * new： 新值
 * Java中基本类型的读写属于原子操作
 * a = 1; 原子操作
 * b = 2; 原子操作
 * a = b; 不是原子操作
 */
public class CASClass {
    private int sum;

    public void add() {
        for (int i = 0; i < 100000; i++) {
            this.sum = this.sum + 1; //非原子操作
            System.out.println(Thread.currentThread().getName());
            System.out.println(this.sum);
        }
    }

    /**
     * 多执行几次
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CASClass casClass = new CASClass();


        Runnable thread1 = () -> casClass.add();

        for (int i = 0; i <5; i++ ) {
            new Thread(thread1, "thread" + i).start();
        }

        Thread.sleep(10000);
        System.out.println("-^-^-^-^-^-^-");
        System.out.println(casClass.sum);

    }

}
