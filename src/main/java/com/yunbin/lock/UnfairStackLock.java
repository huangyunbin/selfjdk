package com.yunbin.lock;

import sun.misc.Unsafe;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by cloud.huang on 17/10/21.
 */
public class UnfairStackLock {
    private Node head;

    private static final Unsafe unsafe = Lock4.getUnsafeInstance();
    private static final long headOffset;

    static final class Node {
        private final Thread thread;
        private volatile Node next;

        public Node(Thread thread, Node next) {
            this.thread = thread;
            this.next = next;
        }

    }


    public void lock() {
        Node node = new Node(Thread.currentThread(), null);
        for (; ; ) {
            if (casHead(null, node)) {
                return;
            }
            Node first = head;
            if (casHead(first, node)) {
                if (first == null) {
                    return;
                }
                node.next = first;
                LockSupport.park();
                return;
            }
        }


    }


    public void unlock() {
        for (; ; ) {
            Node first = head;
            Node next = first.next;
            if (casHead(first, next)) {
                if (first.thread != Thread.currentThread()) {
                    LockSupport.unpark(first.thread);
                }
                return;
            }
        }

    }


    static {
        try {
            headOffset = unsafe.objectFieldOffset
                    (UnfairStackLock.class.getDeclaredField("head"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final boolean casHead(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, expect, update);
    }


}
