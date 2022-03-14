package com.g.estate.school.jvm;

import com.g.estate.school.transaction.AOPSampleClass;

import java.util.ServiceLoader;

public class TestClassLoaderClass {

    static {
        System.out.println(111);

    }

    public static void main(String[] args) throws ClassNotFoundException {

        Class<?> clazz = ServiceLoader.class;

        System.out.println(TestClassLoaderClass.class.getClassLoader());
//        Class.forName("");
    }

}
