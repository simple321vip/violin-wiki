package com.g.estate.school.concurrent;

import java.util.concurrent.ThreadLocalRandom;

public class Demo {

    private final static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();


    public static void main(String[] args) {
        for (int i = 0; i < 10; i ++) {
            new Play().start();
        }
    }

    static class Play extends Thread {
        @Override
        public void run() {
            System.out.println(getName() + ":" + RANDOM.nextInt(10));
        }
    }
}
