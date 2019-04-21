package com.altr.wiley.strategy;

import java.util.*;

public class CustomLFUMapImpl implements EvictionStrategy {

    private int maxSize;
    private HashMap<Object, Object> values;
    private HashMap<Object, Integer> counts;
    private HashMap<Integer, LinkedHashSet<Object>> lists;
    private int min = -1;

    public CustomLFUMapImpl(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("LRUMap max size must be greater than 0");
        } else {
            this.maxSize = maxSize;
            this.values = new HashMap<>();
            this.counts = new HashMap<>();
            this.lists = new HashMap<Integer, LinkedHashSet<Object>>();
            this.lists.put(1, new LinkedHashSet<>());
        }
    }

    public Object get(Object key) {
        if (!this.values.containsKey(key)) {
            return null;
        }
        int count = counts.get(key);
        counts.put(key, count + 1);
        lists.get(count).remove(key);
        if (count == min && lists.get(count).size() == 0)
            min++;
        if (!lists.containsKey(count + 1))
            lists.put(count + 1, new LinkedHashSet<>());
        lists.get(count + 1).add(key);
        return values.get(key);
    }

    public void put(Object key, Object value) {
        if (values.containsKey(key)) {
            values.put(key, value);
            get(key);
            return;
        }
        if (values.size() >= maxSize) {
            Object evit = lists.get(min).iterator().next();
            lists.get(min).remove(evit);
            values.remove(evit);
            counts.remove(evit);
        }

        values.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
    }

    public int size() {
        return values.size();
    }

    public void changeCacheSize(int newSize) {
        if (newSize < maxSize) {
            if (size() > 0) {
                for (int iter = 0; iter < this.maxSize - newSize; iter++) {
                    int minKey = -1;
                    for (Map.Entry<Integer, LinkedHashSet<Object>> entry : this.lists.entrySet()) {
                        if (minKey == -1) {
                            minKey = entry.getKey();
                        } else if (entry.getKey() < minKey) {
                            minKey = entry.getKey();
                        }
                    }
                    Object key = this.lists.get(minKey).iterator().next();
                    lists.remove(minKey);
                    counts.remove(key);
                    values.remove(key);
                }
            }
        }
        this.maxSize = newSize;
    }

}
