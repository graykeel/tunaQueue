package com.queue.tuna.sizeof.cache;

/**
 * Created by zhangtao on 2017/3/12.
 */
public interface CacheEvent {

    CacheOperationOutcomes getCacheOperationOutcome();

    Object getObject();

}
