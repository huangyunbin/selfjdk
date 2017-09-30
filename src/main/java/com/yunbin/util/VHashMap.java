package com.yunbin.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by cloud.huang on 17/9/30.
 */
public class VHashMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    class VHashMapEntry implements Map.Entry {
        private K key;
        private V value;

        public VHashMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
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


    private Object[] array = new Object[16];


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
        LinkedList<VHashMapEntry> list = (LinkedList) array[index];
        if (list == null) {
            return null;
        }
        for (VHashMapEntry one : list) {
            if (one.getKey().equals(key)) {
                return one.getValue();
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = key.hashCode();
        int index = hash % array.length;
//        V old = (V) array[index];
        LinkedList<VHashMapEntry> list = (LinkedList) array[index];
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(new VHashMapEntry(key, value));
        array[index] = list;
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
