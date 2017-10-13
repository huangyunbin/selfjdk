package com.yunbin.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by cloud.huang on 17/10/9.
 */
public class T {

    public static void main(String[] args) {
        final Lock4 lock = new Lock4();


        new Thread() {
            @Override
            public void run() {
                System.out.println("1 lock");
                lock.lock();
                try {
                    TimeUnit.MILLISECONDS.sleep(10);

                    lock.lock();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (Exception e) {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
                System.out.println("1 unlock");

            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                System.out.println("2 lock");

                lock.lock();
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                    lock.lock();
                    try {
                        TimeUnit.MILLISECONDS.sleep(5);
                    } catch (Exception e) {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
                System.out.println("2 unlock");

            }
        }.start();


//        try {
//            TimeUnit.MILLISECONDS.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        new Thread() {
            @Override
            public void run() {
                System.out.println("3 lock");

                lock.lock();
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                    lock.lock();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (Exception e) {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
                System.out.println("3 unlock");

            }
        }.start();


//        new Thread() {
//            @Override
//            public void run() {
//                System.out.println("4 lock");
//
//                lock.lock();
//                try {
//                    TimeUnit.MILLISECONDS.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                lock.unlock();
//                System.out.println("4 unlock");
//
//            }
//        }.start();

    }
}
