package com.g.estate.school.algorithm;

import org.openjdk.jol.info.ClassLayout;

import java.util.HashMap;
import java.util.Map;

public class HashMapClass {

    public static void main(String[] args) {
        Map map = new HashMap();

        System.out.println(ClassLayout.parseInstance(map).toPrintable());
//        java.util.HashMap object internals:
//        OFFSET  SIZE                       TYPE DESCRIPTION                               VALUE
//        0     4                            (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
//        4     4                            (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//        8     4                            (object header)                           a3 37 00 f8 (10100011 00110111 00000000 11111000) (-134203485)
//        12     4              java.util.Set AbstractMap.keySet                        null
//        16     4       java.util.Collection AbstractMap.values                        null
//        20     4                        int HashMap.size                              0
//        24     4                        int HashMap.modCount                          0
//        28     4                        int HashMap.threshold                         0
//        32     4                      float HashMap.loadFactor                        0.75
//        36     4   java.util.HashMap.Node[] HashMap.table                             null
//        40     4              java.util.Set HashMap.entrySet                          null
//        44     4                            (loss due to the next object alignment)
//        Instance size: 48 bytes

        // 从这里我们需要去知道的至少有以下几个
        // 4. 什么是table
        // 5. 什么是loadFactor
        // 1. 什么是keySet
        // 2. 什么是values
        // 3. 什么是entrySet

        // 我们去看源码
        // table java.util.HashMap.Node[] HashMap内部维护的一个Node对象的数组，Node构造器如下
//        static class Node<K,V> implements Map.Entry<K,V> {
//            final int hash; 哈希值
//            final K key; 键 final类型说明，一个Node对象生成他的主键就不会改变，这个主键指的是key对象的引用
//            V value; 值 随时可以修改的值
//            HashMap.Node<K,V> next; 下一个node 这个地方是相同哈希值的时候一链表的形式存储。
        // loadFactor 负载因子 我们知道HasHMap 初始容量是16，这个容量不是别的是Node[]数组的大小，数组是内存中一段连续分配的内存，不可改变。
        // 当我们增加元素超过16个的时候，我们就需要扩容，在内存中重新分配一段内存，并将原来的值复制过来。
        // 所谓的负载因子就是在元素增加到容量的百分比后，就进行扩容的一个参数，
        // 有人会问为什么默认设置成0.75呢，这个需要和扩容倍数2 一起来理解，如果是这个数是0.5， Node数量超过半数，就进行扩容，如果是1，只有数组满了才扩容
        // 以加63 个Node为例，
        // 0.75 时候， 12 进行扩容一次（重新分配32个Node内存） 24进行扩容二次（重新分配64个Node内存）， 48进行扩容二次（重新分配128个Node内存），
        // 0.5 的时候  8 进行扩容一次（重新分配32个Node内存） 16进行扩容二次（重新分配64个Node内存）， 32进行扩容二次（重新分配128个Node内存），64进行扩容二次（重新分配256个Node内存）
        // 1的时候     16 进行扩容一次（重新分配32个Node内存） 32进行扩容二次（重新分配64个Node内存）
        // 显而易见 0.5的时候进行4次扩容，多分配除了 更多的空间。
        // 1的时候显而易见，确实扩容了2次就满足了要求节省了内存空间，但是这个时候就容易发生哈希碰撞。极端情况下63个Node不需要扩容，他们的hash值都是一样的，导致他们都在一个 Node结点位置后形成了
        // 形成了链表的存在。链表众所周知的是查询速度慢的要死。虽然jdk1.8以后，这个链表超过8个，链表结构会变成红黑二叉树的数据结构。
        // 所以说负载因子0.75是在查询时间上和 内存使用空间上的一种平衡。

    }
}
