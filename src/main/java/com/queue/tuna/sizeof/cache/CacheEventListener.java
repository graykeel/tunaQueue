package com.queue.tuna.sizeof.cache;

/**
 * Created by zhangtao on 2017/3/11.
 */
public interface CacheEventListener {
    void handleEvent(CacheEvent cacheEvent);
}
