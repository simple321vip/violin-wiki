package com.g.estate.school.algorithm;

import java.util.*;

public class TreeMapClass {

    public static void main(String[] args) {

        TreeMap map = new TreeMap();

        // TreeMap 里面封装了一个comparator，
        // TreeMap 实现了SortedMap接口，该接口提供了各种排序相关的方法
        map.comparator();
        // TreeMap 是采用红黑二叉树实现的，
        // TreeMap 的使用场景是基于排序的，其时间复杂度是O（logn）
        // 并没有使用シンクロナイズド和ReentrantRock，所以他也是线程非安全的。

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        // LinkedHashMap 的父类 是 HashMap ，

        Set<String> set = new HashSet<>();
        // HashSet 维护了一个HashMap

        // 注意 MAP 是顶级接口 ，Set并不是顶级接口，Set的顶级接口是Iterable，该接口维护了一个Iterator
        // Set 最大的是去重

        // Collection -》list set queue
        // Map







    }


}
