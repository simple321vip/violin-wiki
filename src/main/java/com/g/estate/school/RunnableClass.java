package com.g.estate.school;

import org.openjdk.jol.info.ClassLayout;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;
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
        // 可以看出 join方法本身就是 synchronized的 底层调用的是object的wait方法。
        // 下面的代码例子很好解释了为什么说wait方法调用后，synchronized锁就没有了，另一个对象又可以调用wait方法，形成并联等待状态
        // 如果有synchronized的话，第一个synchronized的 start后面应该是synchronized的 end，如果是这样的话，锁就是串联了。

//        before
//        synchronized的 start
//        synchronized的 start
//        synchronized的 end
//        synchronized的 end
//        -!-!-!-!-!-!-!-!
//                -!-!-!-!-!-!-!-!
        Object object = new Object();
        Runnable runnable2 = () -> {
            synchronized (object) {
                System.out.println("synchronized的 start");
                try {
                    object.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("synchronized的 end");
            }
            System.out.println("-!-!-!-!-!-!-!-!");
        };
        Thread thread = new Thread(runnable2);
        thread.start();

        Thread thread3 = new Thread(runnable2);
        thread3.start();

        System.out.println("before");

        // notify 和 notifyAll 和 wait方法一样 都必须在同步代码块里使用，并且调用后锁就没有了。原因和wait一样。
        synchronized (object) {
            object.notifyAll();
        }

        Thread.currentThread().sleep(3000);


        // 反过来 sleep  这个方法 会直接让出CPU，但不会释放锁我们在测试一下
        Sleep sleep = new Sleep();
        Sleep sleep2 = new Sleep();
        sleep.start();
        sleep2.start();
        System.out.println("sleep.....");
//        synchronized (Sleep.value) {
//            Sleep.value.notify();
//        }

        sleep.sleep(1000);
//        sleep2.sleep(2000);


//        x1
//                x2
//        y3
//                y4



    }

    static class Sleep extends Thread{

        static AtomicLong value = new AtomicLong(1);

        static boolean f = true;

        @Override
        public void run() {
            System.out.println(this.getName() + "~" +value.getAndIncrement());

//            synchronized (value) {
//                try {
//                    value.wait(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            synchronized (value) {
                System.out.println(this.getName() + " enter synchronized" + LocalDateTime.now());
//                if (f) {
//                    f = false;
//                    try {
//                        this.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                for (int i = 0; i < 10000; i++) {
                    System.out.print(1);
                }
                for (int i = 0; i < 100; i++) {
                    System.out.println(this.getName() + "~" + value.getAndIncrement());
                }
                System.out.println(this.getName() + " exit synchronized" + LocalDateTime.now());
            }
        }
    }

}
