package com.yunbin.lock;

import sun.misc.Unsafe;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class Node {
    Thread currentThread;
    Node next;

    public static final Unsafe unsafe = Lock4.getUnsafeInstance();


    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final boolean compareAndSetNext(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }


    public Node(Thread thread) {
        this.currentThread = thread;
    }


}
