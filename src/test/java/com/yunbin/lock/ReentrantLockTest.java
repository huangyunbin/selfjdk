package com.yunbin.lock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by cloud.huang on 17/10/13.
 */
public class ReentrantLockTest {

    static Lock4 lock = new Lock4();

    @Test
    public void reentrantTest() throws Exception {
        lock.lock();
        TimeUnit.MILLISECONDS.sleep(10);
        lock.lock();
        TimeUnit.MILLISECONDS.sleep(10);
        lock.unlock();
        TimeUnit.MILLISECONDS.sleep(10);
        lock.unlock();

    }

}
