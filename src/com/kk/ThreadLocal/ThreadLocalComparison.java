package com.kk.ThreadLocal;

import com.kk.SimpleRunnable;
import com.kk.ThreadLocalRunnable;

public class ThreadLocalComparison {
    public static void main(String[] args) throws InterruptedException {

        // inconsistency acroos multiple threads on same runnable
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

        /** only is consistent across non-static member variables of Runnable
         * memory issue as multiple instances of the Runnable are created
         *
         * inconsistent with static variables on different instances of the runnable across multiple threads
         * as static variables are class level variables
         */
        Runnable runnable1 = new SimpleRunnable();
        Runnable runnable2 = new SimpleRunnable();
        Runnable runnable3 = new SimpleRunnable();
        Thread threadLocalThreadFirst1 = new Thread(runnable1,
                "Different Runnable Instance 1st Thread - ");
        Thread threadLocalThreadSecond2 = new Thread(runnable2,
                "Different Runnable Instance 2nd Thread - ");
        Thread threadLocalThreadThird3 = new Thread(runnable3,
                "Different Runnable Instance 3rd Thread - ");

        threadLocalThreadFirst1.start();
        threadLocalThreadFirst1.join();
        threadLocalThreadSecond2.start();
        threadLocalThreadSecond2.join();
        threadLocalThreadThird3.start();
        threadLocalThreadThird3.join();

        // achieved consistency and avoided memory problems
        Runnable runnable = new ThreadLocalRunnable();
        Thread threadLocalThreadFirst = new Thread(runnable,
                "1st Thread - ThreadLocal - ");
        Thread threadLocalThreadSecond = new Thread(runnable,
                "2nd Thread - ThreadLocal - ");
        Thread threadLocalThreadThird = new Thread(runnable,
                "3rd Thread - ThreadLocal - ");

        threadLocalThreadFirst.start();
        threadLocalThreadFirst.join();
        threadLocalThreadSecond.start();
        threadLocalThreadSecond.join();
        threadLocalThreadThird.start();
        threadLocalThreadThird.join();

    }
}
