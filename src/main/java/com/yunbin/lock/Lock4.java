package com.yunbin.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by cloud.huang on 17/10/11.
 */
public class Lock4 {
    Node head = null;

    private static final Unsafe unsafe = getUnsafeInstance();
    private static final long headOffset;

    static {
        try {
            headOffset = unsafe.objectFieldOffset
                    (Lock4.class.getDeclaredField("head"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final boolean compareAndSetHead(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, expect, update);
    }

    public void lock() {
        System.out.println("lock......");
        Node node = new Node(Thread.currentThread());
        boolean flag = compareAndSetHead(null, node);
        if (flag) {
            return;
        } else {
            Node last = head;
            for (; ; ) {
                if (last.next != null) {
                    last = last.next;
                    continue;
                }
                boolean nextFlag = last.compareAndSetNext(last.next, node);
                if (nextFlag) {
                    LockSupport.park(this);
                    return;
                }
            }
        }
    }


    public void unlock() {
        System.out.println("unlock......");
        for (; ; ) {
            Node next = head.next;
            boolean flag = compareAndSetHead(head, head.next);
            System.out.println("head is " + head);
            if (flag) {
                if (next != null) {
                    LockSupport.unpark(next.currentThread);
                    System.out.println("unpark:" + next.currentThread);
                }
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
