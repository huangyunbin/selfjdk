package com.yunbin.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class Lock3 {

    AtomicBoolean flag = new AtomicBoolean(true);


    public void lock() {
        while (true) {
            boolean result = flag.compareAndSet(true, false);
            if (result) {
                break;
            }
            Thread.yield();
        }
    }


    public void unlock() {
        flag.set(true);
    }
}
