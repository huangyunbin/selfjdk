package com.yunbin.lock;

import sun.misc.Unsafe;

/**
 * Created by cloud.huang on 17/10/7.
 */
public class Node {
    Thread currentThread;
    Node next;

    private static final Unsafe unsafe = Unsafe.getUnsafe();


    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public final boolean compareAndSet(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }


    public Node(Thread thread) {
        this.currentThread = thread;
    }

    public boolean addNext(Node node) {
        boolean result = compareAndSet(next, node);
        return result;


    }


    public void remove(Thread thread) {
        if (next == null) {
            return;
        }
        Node currentNode = this;
        while (true) {
            if (currentNode.next.currentThread == thread) {
                currentNode.compareAndSet(next, next.next);
                break;
            }
            currentNode = currentNode.next;
        }
    }


    public void add(Thread thread) {
        Node newNode = new Node(thread);
        Node currentNode = this;
        while (true) {
            if (currentNode.next == null) {
                boolean flag = currentNode.addNext(newNode);
                if (flag == true) {
                    break;
                }
            }
            currentNode = currentNode.next;
        }
    }


}
