package com.kk;

public class SimpleRunnable implements Runnable {

    private Counter counter = new Counter();
    private static Counter staticCounter = new Counter();

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            counter.increment();
            staticCounter.increment();
        }
        System.out.println(Thread.currentThread().getName() + " count = " + counter.getCount());
        System.out.println(Thread.currentThread().getName() + " static counter - count = " + staticCounter.getCount());
    }
}
