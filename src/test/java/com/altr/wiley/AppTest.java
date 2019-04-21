package com.altr.wiley;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;


public class AppTest 
{

    @Test
    @SuppressWarnings("unchecked")
    public void checkLRUCacheSize() {
        InMemoryCache cache = new InMemoryCache(4, App.Strategy.LRU);
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        cache.put(4, "4");
        cache.put(5, "5");
        cache.put(6, "6");
        cache.put(7, "7");
        Assert.assertEquals(Integer.valueOf(4), Integer.valueOf(cache.size()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkLRUCacheLastRemovedAndFirstExists() {
        InMemoryCache cache = new InMemoryCache(4, App.Strategy.LRU);
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        cache.put(4, "4");
        cache.get(1);
        cache.put(5, "5");
        Assert.assertTrue(cache.get(2) == null && cache.get(1) != null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkLFUCacheSize() {
        InMemoryCache cache = new InMemoryCache(4, App.Strategy.LFU);
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        cache.put(4, "4");
        cache.put(5, "5");
        cache.put(6, "6");
        cache.put(7, "7");
        Assert.assertEquals(Integer.valueOf(4), Integer.valueOf(cache.size()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkLFUCacheLastRemovedAndFirstExists() {
        InMemoryCache cache = new InMemoryCache(4, App.Strategy.LFU);
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        cache.put(4, "4");
        for (int i = 0; i < 10; i++) {
            cache.get(1);
            cache.get(3);
            cache.get(4);
        }
        for (int i = 0; i < 5; i++) {
            cache.get(2);
        }
        cache.put(5, "5");
        Assert.assertTrue(cache.get(2) == null && cache.get(1) != null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkLRUCacheSizeChange() {
        InMemoryCache cache = new InMemoryCache(4, App.Strategy.LRU);
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        cache.put(4, "4");
        cache.get(2);
        cache.changeCacheSize(3);
        Assert.assertTrue(cache.get(1) == null && cache.get(2) != null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkLFUCacheSizeChange() {
        InMemoryCache cache = new InMemoryCache(4, App.Strategy.LFU);
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        cache.put(4, "4");
        cache.get(2);
        cache.get(2);
        cache.get(1);
        cache.get(3);
        cache.changeCacheSize(3);
        Assert.assertTrue(cache.get(4) == null && cache.get(2) != null);
    }

}
