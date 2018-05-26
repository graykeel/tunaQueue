package com.queue.tuna.sizeof.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.queue.tuna.sizeof.ClassIntrospectorTest;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangtao on 2017/3/11.
 */
public class LocalCache<K, V> implements CacheManaged<K, V> {

    private static final long DEFAULT_MAX_CACHE_SIZE = 1000L;

    private static final long DEFAULT_MAX_MEMORY_SIZE = 1024 * 1024 * 100L;

    private final com.google.common.cache.Cache cache = CacheBuilder.newBuilder().weakValues().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(10000).build();

    private Lock lock = new ReentrantLock();

    private CacheEventListener cacheSizeListener = new CacheSizeEventListener(this);

    private Long maxCacheSize = DEFAULT_MAX_CACHE_SIZE;

    private Long maxMemorySize = DEFAULT_MAX_MEMORY_SIZE;

    public LocalCache() {
        this(DEFAULT_MAX_CACHE_SIZE, DEFAULT_MAX_MEMORY_SIZE);
    }

    public LocalCache(Long maxCacheSize, Long maxMemorySize) {
        this.maxCacheSize = maxCacheSize;
        this.maxMemorySize = maxMemorySize;
    }

    @Override
    public Long maxMemorySize() {
        return maxMemorySize;
    }

    @Override
    public Long maxCacheSize() {
        return maxCacheSize;
    }

    @Override
    public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
        return (V) cache.get(key, valueLoader);
    }

    @Override
    public V get(K key) {
        return (V) cache.getIfPresent(key);
    }

    @Override
    public boolean put(K key, final V value) {
        cache.put(key, value);
        cacheSizeListener.handleEvent(new CacheEvent() {
            @Override
            public CacheOperationOutcomes getCacheOperationOutcome() {
                return CacheOperationOutcomes.PutOutcome.PUT;
            }

            @Override
            public Object getObject() {
                return value;
            }
        });
        return true;
    }

    @Override
    public boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }

    @Override
    public boolean remove(final K key) {
        final Object obj = this.get(key);
        cache.invalidate(key);
        cacheSizeListener.handleEvent(new CacheEvent() {
            @Override
            public CacheOperationOutcomes getCacheOperationOutcome() {
                return CacheOperationOutcomes.RemoveOutcome.SUCCESS;
            }

            @Override
            public Object getObject() {
                return obj;
            }
        });
        return true;
    }

    @Override
    public boolean removeAll(Set<? extends K> keys) {
        cache.invalidateAll(keys);
        return true;
    }

    @Override
    public boolean clear() {
        cache.invalidateAll();
        return true;
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        return cache.getAllPresent(keys);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> entries) {
        cache.putAll(entries);
        cacheSizeListener.handleEvent(new CacheEvent() {
            @Override
            public CacheOperationOutcomes getCacheOperationOutcome() {
                return CacheOperationOutcomes.PutOutcome.PUT;
            }

            @Override
            public Object getObject() {
                return entries;
            }
        });
    }

    @Override
    public V putIfAbsent(K key, final V value) {
        try {
            lock.lock();
            if (cache.getIfPresent(key) == null) {
                cache.put(key, value);
                cacheSizeListener.handleEvent(new CacheEvent() {
                    @Override
                    public CacheOperationOutcomes getCacheOperationOutcome() {
                        return CacheOperationOutcomes.PutOutcome.PUT;
                    }

                    @Override
                    public Object getObject() {
                        return value;
                    }
                });
            } else {
                return (V) cache.getIfPresent(key);
            }
        } finally {
            lock.unlock();
        }
        return value;
    }

    @Override
    public Iterator<V> iterator() {
        return cache.asMap().values().iterator();
    }

    public static void main(String[] args) throws ExecutionException {
        Cache<String, List> cache = new LocalCache();
        List<ObjectA> objectAs = new ArrayList<ObjectA>();
        objectAs.add(new ObjectA());
        for (int i = 0; i < 1000; i++)
            cache.put("test", objectAs);
    }

    private static class ObjectA {
        String str;  // 4
        int i1; // 4
        byte b1; // 1
        byte b2; // 1
        int i2;  // 4
        LocalCache.ObjectB obj; //4
        byte b3;  // 1
    }

    private static class ObjectB {
        LocalCache.ObjectC objectC = new LocalCache.ObjectC();
    }

    private static class ObjectC {
        LocalCache.ObjectD[] array = new LocalCache.ObjectD[2];

        public ObjectC() {
            array[0] = new LocalCache.ObjectD();
            array[1] = new LocalCache.ObjectD();
        }
    }

    private static class ObjectD {
        int value;
    }
}
