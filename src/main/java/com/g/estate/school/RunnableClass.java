package com.g.estate.school;

import org.openjdk.jol.info.ClassLayout;

import java.util.stream.IntStream;

public class RunnableClass {

    /*
        这属于重新学习。工作中确实也是这样。正如Runnable接口doc所说的那样
        In most cases, the <code>Runnable</code> interface should
        * be used if you are only planning to override the <code>run()</code>
        * method and no other <code>Thread</code> methods.
        * This is important because classes should not be subclassed
        * unless the programmer intends on modifying or enhancing the fundamental
        * behavior of the class.
     */
    public static void main(String[] args) throws InterruptedException {

        Runnable runnable = () -> System.out.println(Thread.currentThread().getName());

        // 是非要注意的是，run方法 只是一个接口方法，跟其他接口本质上没有任何区别，
        // Thread才是他的实现，如果开启一个线程那么只有thread.start(),调用run方法并不会开启线程。
        IntStream.range(0, 10).forEach(e -> {
            new Thread(runnable).start();
        });
        // 相反Thread类在jdk1.7之后加入了随机数种子，这在ThreadLocalRandomClass里面会有说明。
        // 我们可以看到一个Thread对象需要376bytes的内存大小
        System.out.println(ClassLayout.parseInstance(new Thread(runnable)).toPrintable());
        // 打印内容 看到，找到几个重要属性
        // priority 值默认为 5
        // threadLocalRandomSeed 种子 默认0
        // daemon 默认是false
        // threadStatus 默认是 0
        // stackSize 默认是 0
        // group 线程组 默认非空。


        // 1.构造器。很多构造但是都调用了init方法。
        // init参数（ThreadGroup g, Runnable target, String name, long stackSize）
        // 2.JNI native方法有setPriority0， stop0， interrupt0，currentThread，yield，sleep，start0，wait
        // 3. 先说start方法
        // 3.面试中经常问的问题sleep join wait方法，没有几个从第底层代码来讲的，都是当初学的时候老师告诉的。问题是如实观照，老师告诉你的就是真的么
        // 先看join方法
        //  join(long millis) -》会去做 while (isAlive()) 处理 isAlive是 native方法：确保线程是活的。
        // 然后 做了下面的处理
//        long delay = millis - now; 计算耽误时间，
//        if (delay <= 0) { 如果耽误时间小于等于0 就跳出while循环，调用native wait方法，计算当前now值确保下一次循环中now值为最新的
//            break;
//        }
//        wait(delay);
//        now = System.currentTimeMillis() - base;
        Object object = new Object();
        Runnable runnable2 = () -> {
            synchronized (object) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-!-!-!-!-!-!-!-!");
        };
        Thread thread = new Thread(runnable2);
        thread.start();
        System.out.println("before");
        object.notify();



    }

}
