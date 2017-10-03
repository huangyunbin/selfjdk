package com.yunbin.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by cloud.huang on 17/9/30.
 */
public class VHashMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    public VHashMap() {
        array=new Node[16];
    }

    static class Node<K, V>  implements Map.Entry<K, V>  {
         K key;
         V value;
         Node next;

        public Node(){}

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node next() {
            return next;
        }


        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }


        public Object setValue(Object value) {
            V old = this.value;
            this.value = (V) value;
            return old;
        }
    }


    private Node[] array;


    private int size;

    public static void main(String[] args) {

    }

    @Override
    public int size() {
        return size;

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = key.hashCode();
        int index = hash % array.length;
        for (Node<K, V> e = array[index]; e != null; e = e.next()) {
            if (e.getKey().equals(key)) {
                return e.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = key.hashCode();
        int index = hash % array.length;
        Node node = new Node(key, value, array[index]);
        array[index] = node;
        ++size;
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
