package com.g.estate.school;

import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;

public class OffsetClass {

    static class A {

        private int x = 10;

        private B b = new B();
    }

    static class B {
        private int y = 20;
    }

    /**
     * jdk8 默认开启指针压缩，通过打印我们看到：
     * 普通的对象的对象头结构为 三个 object header 加一起一共为 12个字节。
     * 我们会看到 static 静态常量并没有打印出来。说明他不是对象所有的。对象也没有直接的指针指向静态变量。
     * 涉及到偏移量的知识我们复习以下java基本类型的字节大小问题：
     * 整数类型带符号的
     * int 4个字节 即 00000000 00000000 00000000 00000000  -》0
     *               01111111 11111111 11111111 11111111  -》31 次方 - 1
     * short 2个字节-32768～32767
     * long 8个字节
     * byte 一个字节
     *
     * 浮点类型
     * float 4个字节
     * double 8个字节
     *
     * 字符型 不带符号和short的（0~65535）
     * char 2个字节
     *
     * 布尔型
     * boolean 1个字节
     *
     * 有人会说了 int的1 和 Boolean1 是怎么回事不能相等么？ 字节大小都不一样能相等么？
     * int的1 在内存是这样：00000000 00000000 00000000 00000001
     * boolean的1是这样的：00000001
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        String t = new String();
        System.out.println(ClassLayout.parseInstance(t).toPrintable());
        StringBuilder s = new StringBuilder();
        System.out.println(ClassLayout.parseInstance(s).toPrintable());

        A a = new A();
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        System.out.println(ClassLayout.parseInstance(a.b).toPrintable());

        List<String> list = new ArrayList<>();
        System.out.println(ClassLayout.parseInstance(list).toPrintable());

    }

//    java.lang.String object internals:
//    OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
//        0     4          (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
//        4     4          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//        8     4          (object header)                           da 02 00 f8 (11011010 00000010 00000000 11111000) (-134216998)
//       12     4   char[] String.value                              []
//       16     4      int String.hash                               0
//       20     4          (loss due to the next object alignment)
//    Instance size: 24 bytes
//    Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

//    java.lang.StringBuilder object internals:
//    OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
//         0     4          (object header)      markword             01 00 00 00 (00000001 00000000 00000000 00000000) (1)
//         4     4          (object header)      markword             00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//         8     4          (object header)      klass pointer        4b 1b 00 f8 (01001011 00011011 00000000 11111000) (-134210741)
//        12     4      int AbstractStringBuilder.count               0
//        16     4   char[] AbstractStringBuilder.value               [ ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ,  ]
//        20     4          (loss due to the next object alignment)   //Padding 对齐填充数据
//    Instance size: 24 bytes
//    Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
    // markword 部分主要存储对象自身的运行时数据，比如hashcode，gc分代年龄等。
    // 长度为jvm一个word的大小。64位jvm是64bit的。这部分只是粗滤了解，以后有时间在学习
    // klass pointer 对象指向他的元数据（类）的指针，虚拟机通过这个指针来判定是哪个类的实例，但并不是所有的虚拟机都是这样的方式，也可以透过句柄池访问。
    // 在java运行时，会通过线程独有的虚拟机栈中本地变量表引用数据来操作堆上的对象。
    // 虚拟机不同，引用操作对象的方式也不同：操作方式一般分为2类：
    // 句柄池和直接指针
    // 句柄池： 会在堆中开辟一块内存作为句柄池，句柄中存储了对象实例数据的内存地址。
    //      优点：是引用存储的是稳定的句柄地址，引用自身不需要改变
    //      缺点：增加了一次指针定位的资源消耗
    // 指针： 引用直接存储对象在堆中的地址
    //      优点：节省了一次指针定位的开销
    //      缺点：对象被移动时（GC后内存重新修改），引用自身需要被修改，

    // 对其填充字节：很简单，对象占的内存大小因该为8byte的倍数，我看的文章说8bit，我举得这个是有问题的，8bit就是一个byte

    // 再返回来我们学习偏移量或者说偏移地址 OFFSET 是什么 简单来说就是 引用存放的是对象的地址





}
