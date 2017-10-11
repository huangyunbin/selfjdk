package com.yunbin.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by cloud.huang on 17/10/11.
 */
public class Lock4 {
    Node head = null;
    Thread winThread = null;

    private static final Unsafe unsafe = getUnsafeInstance();
    private static final long headOffset;
    private static final long winThreadOffset;

    static {
        try {
            headOffset = unsafe.objectFieldOffset
                    (Lock4.class.getDeclaredField("head"));
            winThreadOffset = unsafe.objectFieldOffset
                    (Lock4.class.getDeclaredField("winThread"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final boolean compareAndSetHead(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, expect, update);
    }

    public final boolean compareAndSetWinThread(Thread expect, Thread update) {
        return unsafe.compareAndSwapObject(this, winThreadOffset, expect, update);
    }

    public void lock() {
        Node newNode = new Node(Thread.currentThread());
        Node target = head;
        for (; ; ) {
            boolean flag = compareAndSetWinThread(null, Thread.currentThread());
            if (flag) {
                return;
            }

            if (target == null) {
                if (compareAndSetHead(null, newNode)) {
                    return;
                } else {
                    continue;
                }
            }

            if (target.next != null) {
                target = target.next;
                continue;
            }
            if (target.compareAndSetNext(null, newNode)) {
                System.out.println("park:" + Thread.currentThread());
                LockSupport.park(this);
                return;
            }
        }
    }


    public void unlock() {
        System.out.println("unlock....");
        for (; ; ) {
            if (head == null ) {
                boolean flag = compareAndSetWinThread(Thread.currentThread(), null);
                if (flag) {
                    return;
                } else {
                    continue;
                }
            }

            Node target = head;
            Thread thread = target.currentThread;
            Thread oldThread = target.currentThread;
            Node targetNext = target.next;
            if (compareAndSetHead(target, targetNext)) {
                System.out.println("unpark:" + thread);
                LockSupport.unpark(thread);
                compareAndSetWinThread(oldThread, thread);
                return;
            }
        }
    }


    public static Unsafe getUnsafeInstance() {
        try {
            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            return (Unsafe) theUnsafeInstance.get(Unsafe.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
