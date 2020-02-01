package com.kk.InheritableThreadLocal;

public class InheritableThreadLocalComparison {
    public static void main(String[] args) {
        Runnable parentRunnable = new ParentRunnable();
        Thread parentThread = new Thread(parentRunnable);
        parentThread.start();
    }
}


class ParentRunnable implements Runnable {
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();

    @Override
    public void run() {
        Thread.currentThread().setName("Parent Thread");
        threadLocal.set(100);
        inheritableThreadLocal.set(100);
        System.out.println(Thread.currentThread().getName() + " - thread local - value = " + threadLocal.get());
        System.out.println(Thread.currentThread().getName() + " - inheritable thread local - value = " + inheritableThreadLocal.get());
        Runnable childRunnable = new ChildRunnable();
        Thread childThread = new Thread(childRunnable);
        childThread.start();
    }
}

class ChildRunnable implements Runnable {
    @Override
    public void run() {
        Thread.currentThread().setName("Child Thread");
        System.out.println(Thread.currentThread().getName() + " - thread local - value = " + ParentRunnable.threadLocal.get());
        System.out.println(Thread.currentThread().getName() + " - inheritable thread local - value = " + ParentRunnable.inheritableThreadLocal.get());
    }
}

