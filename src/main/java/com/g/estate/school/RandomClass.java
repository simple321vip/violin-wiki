package com.g.estate.school;

import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 */
public class RandomClass {

    public static void main(String[] args) {

        // 随机数
        Random random = new Random();
        IntStream.range(0, 10).forEach(e -> {
            System.out.println(random.nextInt(10));
        });

        System.out.println("-^-^-^-^-^-^-^-^-^-^-^-^-");
        // 伪随机数 指定种子后，程序每次执行随机数都是一样的。
        // seed1 -> seed2 > seed3
        //   ↓        ↓       ↓
        // random1 random2　random3
        Random fakeRandom = new Random(0);
        IntStream.range(0, 10).forEach(e -> {
            System.out.println(fakeRandom.nextInt(10));
        });

    }
}
