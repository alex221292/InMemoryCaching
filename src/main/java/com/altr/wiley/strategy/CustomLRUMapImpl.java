package com.altr.wiley.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLRUMapImpl implements EvictionStrategy {
    private int maxSize;
    private static final int DEFAULT_MAX_SIZE = 100;
    private HashMap<Object, Entry> hashmap;
    private Entry start, end;

    public CustomLRUMapImpl() {
        hashmap = new HashMap<Object, Entry>();
    }

    public CustomLRUMapImpl(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("LRUMap max size must be greater than 0");
        } else {
            this.maxSize = maxSize;
            this.hashmap = new HashMap<Object, Entry>();
        }
    }

    public Object get(Object key) {
        if (hashmap.containsKey(key)) // Key Already Exist, just update the
        {
            Entry entry = hashmap.get(key);
            removeNode(entry);
            addAtTop(entry);
            return entry.value;
        }
        return null;
    }

    public void put(Object key, Object value) {
        if (hashmap.containsKey(key)) {
            Entry entry = hashmap.get(key);
            entry.value = value;
            removeNode(entry);
            addAtTop(entry);
        } else {
            Entry newnode = new Entry();
            newnode.left = null;
            newnode.right = null;
            newnode.value = value;
            newnode.key = key;
            if (hashmap.size() >= this.maxSize) {
                hashmap.remove(end.key);
                removeNode(end);
                addAtTop(newnode);

            } else {
                addAtTop(newnode);
            }

            hashmap.put(key, newnode);
        }
    }

    public int size() {
        return hashmap.size();
    }

    private void addAtTop(Entry node) {
        node.right = start;
        node.left = null;
        if (start != null)
            start.left = node;
        start = node;
        if (end == null)
            end = start;
    }

    private void removeNode(Entry node) {

        if (node.left != null) {
            node.left.right = node.right;
        } else {
            start = node.right;
        }

        if (node.right != null) {
            node.right.left = node.left;
        } else {
            end = node.left;
        }
    }

    public void changeCacheSize(int newSize) {
        if (newSize < maxSize) {
            if (size() > 0) {
                ArrayList<Object> keysToRemove = new ArrayList<>();
                for (int iter = 0; iter < this.maxSize - newSize; iter++) {
                    for (Map.Entry<Object, Entry> entry : this.hashmap.entrySet()) {
                        if (entry.getValue().right == null) {
                            removeNode(entry.getValue());
                            keysToRemove.add(entry.getKey());
                        }
                    }
                }
                for (Object key : keysToRemove) {
                    this.hashmap.remove(key);
                }
            }
        }
        this.maxSize = newSize;
    }

}
