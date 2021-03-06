package com.yunbin.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by cloud.huang on 17/10/11.
 */
public class Lock4 {
    volatile Node head = null;
    volatile Node tail = null;
    volatile Thread winThread = null;

    private int state = 0;

    private static final Unsafe unsafe = getUnsafeInstance();
    private static final long headOffset;
    private static final long tailOffset;
    private static final long stateOffset;


    public void lock() {
        System.out.println("lock....");
        if (state > 0 && winThread == Thread.currentThread()) {
            state++;
            return;
        }
        if (compareAndSetState(0, 1)) {
            winThread = Thread.currentThread();
            return;
        }
        Node newNode = new Node(Thread.currentThread());
        final Node node = addWaiter(newNode);
        for (; ; ) {
            if (compareAndSetState(0, 1)) {
                head = node;
                node.currentThread = null;
                node.prev = null;
                winThread = Thread.currentThread();
                return;
            }
            System.out.println("park:" + Thread.currentThread());
            LockSupport.park(this);
        }
    }

    private Node addWaiter(Node node) {
        for (; ; ) {
            Node t = tail;
            if (t == null) {
                Node h = new Node(null);
                h.next = node;
                node.prev = h;
                if (compareAndSetHead(null, h)) {
                    tail = node;
                    return node;
                }
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return node;
                }
            }
        }

    }


    public void unlock() {
        System.out.println("unlock....");
        if (state <= 0 || winThread != Thread.currentThread()) {
            throw new RuntimeException("unlock error");
        }
        state--;
        if (state == 0) {
            winThread = null;
            if (head != null) {
                Node targetNext = head.next;
                if (targetNext != null) {
                    System.out.println("unpark:" + targetNext.currentThread);
                    LockSupport.unpark(targetNext.currentThread);
                }
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

    static {
        try {
            headOffset = unsafe.objectFieldOffset
                    (Lock4.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (Lock4.class.getDeclaredField("tail"));
            stateOffset = unsafe.objectFieldOffset
                    (Lock4.class.getDeclaredField("state"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final boolean compareAndSetHead(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, expect, update);
    }

    public final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }


    public final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }


}
