package com.altr.wiley.strategy;

public interface EvictionStrategy {

    public Object get(Object key);
    public void put(Object key, Object value);
    public int size();
    public void changeCacheSize(int newSize);

}
