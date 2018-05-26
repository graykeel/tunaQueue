package com.queue.tuna.sizeof.cache;

/**
 * Created by zhangtao on 2017/3/11.
 */
public interface CacheManaged<K,V> extends Cache<K,V> {

    Long maxMemorySize();

    Long maxCacheSize();

}
