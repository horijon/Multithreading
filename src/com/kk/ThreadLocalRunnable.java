package com.kk;

public class ThreadLocalRunnable implements Runnable {
    private ThreadLocal<Counter> counter = ThreadLocal.withInitial(() -> new Counter());
    private static ThreadLocal<Counter> staticCounter = ThreadLocal.withInitial(() -> new Counter());

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            counter.get().increment();
            staticCounter.get().increment();
        }
        System.out.println(Thread.currentThread().getName() + " - count = " + counter.get().getCount());
        System.out.println(Thread.currentThread().getName() + " - static counter - count = " + staticCounter.get().getCount());
    }
}
