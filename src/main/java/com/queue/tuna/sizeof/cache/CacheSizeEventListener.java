package com.queue.tuna.sizeof.cache;

import com.queue.tuna.sizeof.ClassIntrospector;
import com.queue.tuna.sizeof.ObjectInfo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhangtao on 2017/3/12.
 */
public class CacheSizeEventListener implements CacheEventListener {

    private CacheManaged cacheManaged;

    private final BlockingQueue<CacheEvent> queue = new LinkedBlockingQueue<CacheEvent>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ObjectSizeCustomer objectSizeCustomer = new ObjectSizeCustomer(queue,cacheManaged);

    public CacheSizeEventListener(CacheManaged cacheManaged) {
        this.cacheManaged = cacheManaged;
        executorService.execute(objectSizeCustomer);
    }

    @Override
    public void handleEvent(CacheEvent cacheEvent) {
        queue.add(cacheEvent);
    }

    private static class ObjectSizeCustomer implements Runnable {

        private BlockingQueue<CacheEvent> queue;

        private CacheManaged cacheManaged;

        private final ClassIntrospector ci = new ClassIntrospector();

        ObjectSizeCustomer(BlockingQueue queue, CacheManaged cacheManaged) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    CacheEvent object = queue.take();
                    if (object.getObject() != null) {
                        ObjectInfo res = ci.introspect(object.getObject());
                        System.out.println("size :" + res.getDeepSize());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
