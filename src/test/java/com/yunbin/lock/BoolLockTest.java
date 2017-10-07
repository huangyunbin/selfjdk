package com.yunbin.lock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class BoolLockTest {
    static AtomicInteger num = new AtomicInteger();
    int sleepMs = 10;

    private Boolean isOn = false;
    private String statusMessage = "I'm off";

    public void doSomeStuffAndToggleTheThing() {
        synchronized (isOn) {
            if (!isOn) {
                isOn = true;
                statusMessage = "I'm on";

            } else {
                isOn = false;
                statusMessage = "I'm off";
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num.getAndIncrement();
        }
    }

    @Test
    public void test() throws Exception {

        long start = System.currentTimeMillis();
        int times = 200;

        for (int i = 0; i < times; i++) {
            new Thread() {
                @Override
                public void run() {
                    doSomeStuffAndToggleTheThing();
                }
            }.start();
        }

        while (true) {
            if (num.get() == times) {
                break;
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }

        //用时是1200左右，就是预期的一半多一点，因为true和false是不同的对象。
        System.out.println(System.currentTimeMillis() - start);
        assertThat(System.currentTimeMillis() - start).isLessThan(times * sleepMs);


    }
}
