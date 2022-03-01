package com.g.estate.school.algorithm;

public class SingonClass {

    // 单例模式，只能存在一个对象，也就说明了 不可以对此new,提供获得对象的方法

    private SingonClass() {

    }

    public static SingonClass get() {

        return new SingonClass();
    }
}
