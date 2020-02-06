package com.kk.CompletableFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static void executeInParallel(List<Runnable> runnableTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        CompletableFuture<?>[] futures = runnableTasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executorService))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        executorService.shutdown();
    }
}
