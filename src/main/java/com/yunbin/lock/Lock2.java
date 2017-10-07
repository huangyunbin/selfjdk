package com.yunbin.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class Lock2 {
    static int SIZE = 300;
    Thread[] threads = new Thread[SIZE];
    AtomicInteger num = new AtomicInteger();
    AtomicInteger total = new AtomicInteger();


    public void lock() {
        num.getAndIncrement();
        int index = total.getAndIncrement();
//        System.out.println("index:" + index);
        index = index % SIZE;
        if (threads[index] != null) {
            System.out.println("duble===");
        }
        System.out.println("index:" + index);
        threads[index] = Thread.currentThread();
        if (index > 0) {
//            System.out.println("park===");
            LockSupport.park();
        }
    }


    public void unlock() {
        num.getAndDecrement();
        System.out.println("unlock:===" + num.get());
        for (int i = 0; i < SIZE; i++) {
            if (threads[i] == Thread.currentThread()) {
                threads[i] = null;
                System.out.println("null===");
                break;
            }

        }

        for (int i = 0; i < SIZE; i++) {
            if (threads[i] == null) {
                continue;
            }
//            System.out.println("unpark...");
            LockSupport.unpark(threads[i]);
            break;
        }
    }

}
