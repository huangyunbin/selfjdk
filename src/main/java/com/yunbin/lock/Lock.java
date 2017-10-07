package com.yunbin.lock;

import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class Lock {
    Vector<Thread> vector = new Vector<Thread>();


    public void lock() {
        vector.add(Thread.currentThread());
        if (vector.size() > 1) {
            LockSupport.park();
        }
    }


    public void unlock() {
        vector.remove(Thread.currentThread());
        if (vector.size() > 0) {
            LockSupport.unpark(vector.get(0));
        }
    }

}
