package com.yunbin.lock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class LockTest {
    static int num = 0;

    @Test
    public void lockTest() throws Exception {
        num = 0;
        final List<Thread> threads = new ArrayList<Thread>();
//        List<Thread> endThreads=new ArrayList<Thread>();
        long start = System.currentTimeMillis();
        final Lock lock = new Lock();
        int times = 3;
        for (int i = 0; i < times; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        lock.lock();
                        TimeUnit.MILLISECONDS.sleep(100);
                        System.out.println(System.currentTimeMillis() + "  " + Thread.currentThread());
                        num++;
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
                        threads.remove(Thread.currentThread());
                    }
                }
            };
            threads.add(thread);
            thread.start();
        }

        while (true) {
            if (threads.size() == 0) {
                break;
            }
            TimeUnit.MILLISECONDS.sleep(100);
            if (System.currentTimeMillis() - start > (times + 1) * 100) {
                fail("超过时间了");
            }
            int blockedNum = getBlockedNum(threads);
            if (blockedNum > 0 && blockedNum < times - 1) {
                fail("阻塞线程数目不对,现在为" + blockedNum);
            }
        }


        assertThat(num).isEqualTo(times);

        assertThat(System.currentTimeMillis() - start).isGreaterThanOrEqualTo(times * 100);

    }

    private int getBlockedNum(List<Thread> threads) {
        int result = 0;
        for (Thread thread : threads) {
            if (thread.getState() == Thread.State.BLOCKED) {
                result++;
            }
        }
        return result;
    }

}
