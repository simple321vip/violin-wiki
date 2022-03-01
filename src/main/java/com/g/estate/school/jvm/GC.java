package com.g.estate.school.jvm;

public class GC {

    /*
    GC jvm垃圾回收机制，网上资料很多，
    我自己总结：
    jvm java虚拟机内存：
    分为 新生代 老年代 持久代，其中1.8以后持久代变成了原空间 持久代是存在jvm的一块内存，而元空间是存在本地内存的一块空间。
    edan survivor S0 S1（8：1：1） minor GC 每次edan满了就会GC一次
    老年代 full GC  每次老年代满了，会进行一次full GC
    System.gc() 系统会要求触发full GC 但不一定执行。

    什么对象被GC认为是垃圾
    可达性分析法和程序计数法
    ・可达性分析法；
        采用root节点向下需要找，当next为空时，
    ・程序计数法：此对象有一个引用，即增加一个计数，删除一个引用，减少一个计数，
        只要收集的计数为0时候，才会被gc收集。此算法最致命的时无法解决处理循环引用的问题


    edan
     */
    public static void main(String[] args) {


    }
}
