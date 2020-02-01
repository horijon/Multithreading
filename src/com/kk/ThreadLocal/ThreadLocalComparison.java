package com.kk.ThreadLocal;

import com.kk.SimpleRunnable;
import com.kk.ThreadLocalRunnable;

public class ThreadLocalComparison {
    public static void main(String[] args) throws InterruptedException {
        SimpleRunnable simpleRunnable = new SimpleRunnable();
        Thread threadFirst = new Thread(simpleRunnable, "1st Thread -");
        Thread threadSecond = new Thread(simpleRunnable, "2nd Thread -");
        Thread threadThird = new Thread(simpleRunnable, "3rd Thread -");
        threadFirst.start();
        threadFirst.join();
        threadSecond.start();
        threadSecond.join();
        threadThird.start();
        threadThird.join();

        Runnable runnable = new ThreadLocalRunnable();
        Thread threadLocalThreadFirst = new Thread(runnable, "1st Thread - com.kk.ThreadLocal -");
        Thread threadLocalThreadSecond = new Thread(runnable, "2nd Thread - com.kk.ThreadLocal -");
        Thread threadLocalThreadThird = new Thread(runnable, "3rd Thread - com.kk.ThreadLocal -");
        threadLocalThreadFirst.start();
        threadLocalThreadFirst.join();
        threadLocalThreadSecond.start();
        threadLocalThreadSecond.join();
        threadLocalThreadThird.start();
        threadLocalThreadThird.join();
    }
}


