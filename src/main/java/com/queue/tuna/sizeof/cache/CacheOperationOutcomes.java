package com.queue.tuna.sizeof.cache;

/**
 * Created by zhangtao on 2017/3/12.
 */
public interface CacheOperationOutcomes {

    enum GetOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    enum RemoveOutcome implements CacheOperationOutcomes {
        SUCCESS,
        FAILURE
    }

    enum PutOutcome implements CacheOperationOutcomes {
        PUT,
        UPDATE,
        NOOP,
        FAILURE
    }
}
