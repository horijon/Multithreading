package com.kk.ThreadPoolThreadLocal;

import com.kk.ThreadLocalRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalUsingThreadPool {
    public static void main(String[] args) throws InterruptedException {
        Runnable threadLocalRunnable = new ThreadLocalRunnable();
        Runnable threadLocalUsingThreadPoolRunnable = new ThreadLocalUsingThreadPoolRunnable();
        ExecutorService executorServiceWrongRunnable = Executors.newFixedThreadPool(2);
        executorServiceWrongRunnable.submit(threadLocalRunnable);
        executorServiceWrongRunnable.submit(threadLocalRunnable);
        executorServiceWrongRunnable.submit(threadLocalRunnable);
        executorServiceWrongRunnable.shutdown();
        // just to print next line after above operations
        executorServiceWrongRunnable.awaitTermination(2L, TimeUnit.SECONDS);
        System.out.println();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(threadLocalUsingThreadPoolRunnable);
        executorService.submit(threadLocalUsingThreadPoolRunnable);
        executorService.submit(threadLocalUsingThreadPoolRunnable);
        executorService.shutdown();
    }
}