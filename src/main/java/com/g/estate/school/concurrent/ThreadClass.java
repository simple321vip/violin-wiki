package com.g.estate.school.concurrent;

public class ThreadClass {

    /**
     * 守护线程 ，其他线程结束后，守护线程就会自动结束，所以守护线程不适合读写资源，因为很容易造成数据丢失，
     * JVM中典型的应用就是GC线程，GC线程负责垃圾回收，当系统中不存在其他线程工作，也就没有垃圾可以回收了。自动结束
     * @param args
     */
    public static void main(String[] args) {

        // 守护线程一定是先设定了setDaemon true 后在start开启线程，否则是会报以下错的。
        Thread t = Thread.currentThread();
        try {
            t.setDaemon(true);
        } catch (IllegalThreadStateException e) {
            System.out.println("-^-^-^-^-^-^-^-^-^-^-^");
            // e.printStackTrace 指定的输出流是System.err
            // System.out.println 的输出流是System.out
            // 不指定输出流的话，log日志的顺序是不可定的。
            e.printStackTrace(System.out);

            System.out.println("-^-^-^-^-^-^-^-^-^-^-^");
        } finally {
            Thread child = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            this.wait(1000);
                        }
                        System.out.println("I promiss I is not the last print ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            child.start();
            System.out.println("execute before");
            Thread daemon = new Thread() {
                @Override
                public void run() {
                    int i = 0;
                    while (true) {
                        if (i > 50) {
                            System.out.print("1");
                            i = 0;
                        } else {
                            i++;
                        }


                    }
                }
            };
            daemon.setDaemon(true);
            daemon.start();

        }

        // Exception in thread "main" java.lang.IllegalThreadStateException
        //	at java.lang.Thread.setDaemon(Thread.java:1359)
        //	at com.g.estate.school.concurrent.ThreadClass.main(ThreadClass.java:14)



    }
}
