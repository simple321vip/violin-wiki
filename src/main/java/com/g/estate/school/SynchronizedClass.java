package com.g.estate.school;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SynchronizedClass {

    Queue<String> queue = new LinkedList<>();

    public static List list = new ArrayList();

    /**
     * 锁这个地方没有什么好说的，函数锁和对象锁，重要的还是死锁问题
     * 首先锁是可以同时存在多个的。
     * 比如说 在加了锁a代码块里 有调用了加了b的代码块，锁的数量就是2个，如果b退出了锁代码块，锁的数量就变成了一个
     * 在获取多个锁的时候，不同线程获取了不同对象的锁，就有可能形成死锁。
     * 简单来说  线程1获取对象锁后 准备获取b对象锁，与此同时线程2获取了b对象锁，准备获取a对象锁。
     * 比如说 1班的女学生学生转班去2班，同时2班的男同学转班去1班
     * 以两个班级为对象，
     * synchronized (class1) {
     *      System.out.println("1班的女学生学生转班去2班");
     *     classs1.count = classs1.count 1 1
     *     synchronized (class2) {
     *         classs2.count = classs2.count + 1
     *     }
     * }
     *      * synchronized (class2) {
     *      *      System.out.println("1班的女学生学生转班去2班");
     *      *     classs2.count = classs2.count - 1
     *      *     synchronized (class1) {
     *      *         classs1.count = classs1.count + 1
     *      *     }
     *      * }
     *  此时，两个线程各自持有不同的锁，然后各自试图获取对方手里的锁，造成了双方无限等待下去，这就是死锁。
     *
     * 死锁发生后，没有任何机制能解除死锁，只能强制结束JVM进程。
     * 编程的时候
     * 线程获取锁的顺序要一致。即严格按照先获取lockA，再获取lockB的顺序
     *
     *
     * @param args
     */
    public static void main(String[] args) {

        Object object = new Object();

        // 对象锁 一个线程可以对同一个一个对象加锁
        synchronized (object) {
            System.out.println(1);
            synchronized(object) {
                System.out.println(2);
            }
        }

        SynchronizedClass synchronizedClass = new SynchronizedClass();
        // this 锁 也是对象锁 ，下面方法中thread2的addTask方法永远不会执行，因为在线程1中已经对synchronizedClass获取了锁的状态
//        Thread thread1 = new Thread() {
//            @Override
//            public void run() {
//                synchronizedClass.getTask();
//            }
//        };
//        thread1.start();
//
//        Thread thread2 = new Thread() {
//            @Override
//            public void run() {
//                synchronizedClass.addTask("ssss");
//            }
//        };
//        thread2.start();

        // 类锁，会锁住该类的所有对象, 在线程3里对SynchronizedClass类加了锁，导致，在线程4里，一直获取锁失败。
        Thread thread3 = new Thread() {
            @Override
            public void run() {
                SynchronizedClass.getItem();
            }
        };
        thread3.start();

        Thread thread4 = new Thread() {
            @Override
            public void run() {
                SynchronizedClass.add();
            }
        };
        thread4.start();


    }

    public synchronized void addTask(String s) {
        this.queue.add(s);
    }

    public synchronized String getTask() {
        while (queue.isEmpty()) {

        }
        return queue.remove();
    }

    public static synchronized Object getItem() {

        while (list.isEmpty()) {

        }
        return list.get(0);
    }

    public static synchronized void add() {
        list.add("");
    }


    // 代码块和静态代码块是无法加锁的
    static {


    }



}
