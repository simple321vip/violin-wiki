package com.g.estate.school.concurrent;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomClass {

    public static void main(String[] args) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        Runnable runnable = () -> {
                System.out.println(threadLocalRandom.nextInt(10));
        };

        for (int i = 0; i < 10; i ++) {

            new Thread(runnable).start();
        }


    }
}
