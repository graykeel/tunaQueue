package com.queue.tuna.sizeof.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by zhangtao on 2017/3/11.
 */
public interface Cache<K, V> extends Iterable<V> {

    V get(K key,Callable<? extends V> valueLoader) throws ExecutionException;

    V get(K key);

    boolean put(K key, V value);

    boolean containsKey(K key);

    boolean remove(K key);

    boolean removeAll(Set<? extends K> keys);

    boolean clear();

    Map<K, V> getAll(Set<? extends K> keys);

    void putAll(Map<? extends K, ? extends V> entries);

    V putIfAbsent(K key, V value);

}
