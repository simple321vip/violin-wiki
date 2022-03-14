package com.g.estate.school.jvm;

import org.openjdk.jol.info.ClassLayout;

import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClassLoaderClass {

    public static int sum = 2;

    // java.lang.ClassNotFoundExcetpion

    /**
     * there are same concepts in the following.
     *
     * SPI: Service Provider Interface, these interfaces are defined in rt.jar and
     * permit the third party like JDBC and JNDI to implement SPI.
     *
     *
     *
     */
    public static void main(String[] args) {

        // what is different of the following three clazz
        try {
            Class<?> clazz1 = TestClassLoaderClass.class;
//            System.out.println(ClassLayout.parseInstance(clazz1).toPrintable());
            System.out.println(clazz1);
            System.out.println(clazz1);
            Class<?> clazz2 = Class.forName("com.g.estate.school.jvm.TestClassLoaderClass");
            System.out.println(clazz2);
            Class<?> clazz3 = ClassLoader.getSystemClassLoader().loadClass("com.g.estate.school.jvm.TestClassLoaderClass");
            System.out.println(clazz3);
            Class.forName("org.h2.Driver");
            DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/imooc", "root", "root");


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

}
