package com.altr.wiley;

import com.altr.wiley.strategy.CustomLFUMapImpl;
import com.altr.wiley.strategy.CustomLRUMapImpl;
import com.altr.wiley.strategy.EvictionStrategy;

public class InMemoryCache<K, T> {

    private EvictionStrategy cacheMap;

    protected class CacheObject {
        T value;

        CacheObject(T value) {
            this.value = value;
        }
    }

    InMemoryCache(int maxItems, App.Strategy strategy) {
        if (App.Strategy.LRU == strategy) {
            cacheMap = new CustomLRUMapImpl(maxItems);
        } else if (App.Strategy.LFU == strategy) {
            cacheMap = new CustomLFUMapImpl(maxItems);
        } else {
            throw new IllegalArgumentException("PLEASE SPECIFY APPLICABLE STRATEGY");
        }
    }

    public void put(K key, T value) {
        cacheMap.put(key, new CacheObject(value));
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        CacheObject c = (CacheObject) cacheMap.get(key);

        if (c == null)
            return null;
        else {
            return c.value;
        }
    }

    public int size() {
        return cacheMap.size();
    }

    public void changeCacheSize(int newSize) {
        cacheMap.changeCacheSize(newSize);
    }

}
