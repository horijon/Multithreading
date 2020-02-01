package com.kk.ThreadPoolThreadLocal;

import com.kk.Counter;

public class ThreadLocalUsingThreadPoolRunnable implements Runnable {
    private ThreadLocal<Counter> counterThreadLocal = ThreadLocal.withInitial(() -> new Counter());
    private static ThreadLocal<Counter> staticCounterThreadLocal = ThreadLocal.withInitial(() -> new Counter());

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                counterThreadLocal.get().increment();
                staticCounterThreadLocal.get().increment();
            }
            System.out.println(Thread.currentThread().getName() + " - count = " + counterThreadLocal.get().getCount());
            System.out.println(Thread.currentThread().getName() + " - static counter - count = " + staticCounterThreadLocal.get().getCount());
        } finally {
            counterThreadLocal.remove();
            staticCounterThreadLocal.remove();
        }
    }
}
