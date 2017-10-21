package com.yunbin.lock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class LockTest {
    static AtomicInteger num = new AtomicInteger();
    static UnfairStackLock lock = new UnfairStackLock();

    @Test
    public void lockTest2() throws Exception {
        for (int i = 0; i < 10; i++) {
            lockTest();
        }
    }

    @Test
    public void lockTest() throws Exception {
        num = new AtomicInteger();
        final List<Thread> threads = new ArrayList<Thread>();
        long start = System.currentTimeMillis();

        int threadNums = 200;
        final int sleepMs = 2;
        for (int i = 0; i < threadNums; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        lock.lock();
                        TimeUnit.MILLISECONDS.sleep(sleepMs);
                        System.out.println(System.currentTimeMillis() + "  " + Thread.currentThread());
                        num.getAndIncrement();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            };
            threads.add(thread);
            thread.start();
        }

        while (true) {
            if (num.get() == threadNums) {
                break;
            }
            TimeUnit.MILLISECONDS.sleep(100);
            if (System.currentTimeMillis() - start > threadNums * sleepMs + 2000) {
                fail("超过时间了  " + num + "   " + threads.size());
            }
            int runningNum = getRunningNum(threads);
            if (runningNum > 1) {
                fail("运行线程数目不对,现在为" + runningNum);
            }
        }


        assertThat(num.get()).isEqualTo(threadNums);
        System.out.println("use time:" + (System.currentTimeMillis() - start));

        assertThat(System.currentTimeMillis() - start).isGreaterThanOrEqualTo(threadNums * sleepMs);

    }

    private int getRunningNum(List<Thread> threads) {
        int result = 0;
        for (Thread thread : threads) {
            if (thread.getState() == Thread.State.RUNNABLE) {
                result++;
            }
        }
        return result;
    }

}
