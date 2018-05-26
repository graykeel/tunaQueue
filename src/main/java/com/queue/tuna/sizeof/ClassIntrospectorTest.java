package com.queue.tuna.sizeof;

import org.apache.commons.collections.CollectionUtils;

import java.lang.management.ThreadInfo;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by zhangtao on 2017/3/4.
 */
public class ClassIntrospectorTest {
    static Map<String, List<ObjectA>> cache = new WeakHashMap<String, List<ObjectA>>();

    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    static BlockingQueue<TestThreadInfo> threadQueue = new LinkedBlockingQueue<TestThreadInfo>();

    static class TestThreadInfo {

        private Thread thread;

        private Long time;

        public TestThreadInfo(Thread thread,Long time) {
            this.thread = thread;
            this.time = time;
        }

        public Thread getThread() {
            return thread;
        }

        public void setThread(Thread thread) {
            this.thread = thread;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }

    public static void main2(String[] args){
        System.out.println(FibonacciTailRecursive(10,10,10));
    }

    static int FibonacciTailRecursive(int n, int ret1, int ret2) {
        if (n == 0)
            return ret1;
        return FibonacciTailRecursive(n - 1, ret2, ret1 + ret2);
    }

    public static void main(String[] args) throws IllegalAccessException, InterruptedException {

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                TestThreadInfo info = null;
                try {
                    while((info = threadQueue.take())!=null){
                        Thread.sleep(1000-(System.currentTimeMillis()-info.getTime()));
                        info.thread.interrupt();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },1, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    threadQueue.add(new TestThreadInfo(Thread.currentThread(),System.currentTimeMillis()));
                    final ClassIntrospector ci = new ClassIntrospector();
                    ObjectInfo res;
                    List<ObjectA> objectAs = cache.get("test");
                    if (CollectionUtils.isNotEmpty(objectAs)) {
                        Long start = System.nanoTime();
                        System.currentTimeMillis();
                        res = ci.introspect(objectAs);
                        System.out.println("size :"+res.getDeepSize());
                        System.out.println("time :"+(System.nanoTime() - start));
                    }
//                    res = ci.introspect(new ObjectC());
//                    System.out.println("test");
//                System.out.println(cache.get("test"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1000, TimeUnit.MILLISECONDS);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 32; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        put(get("test"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static List<ObjectA> get(String key) {
        if (cache.get("test") == null) {
            return new ArrayList<ObjectA>() {{
                add(new ObjectA());
            }};
        }
        return cache.get("test");
    }

    public static void put(List<ObjectA> objectAList) {
//        for (int i=0;i<1000000;i++){
        if (cache.get("test") == null) {
            cache.put("test", new ArrayList<ObjectA>());
        }
        cache.get("test").addAll(objectAList);
//        }
    }

    private static class ObjectA {
        String str;  // 4
        int i1; // 4
        byte b1; // 1
        byte b2; // 1
        int i2;  // 4
        ObjectB obj; //4
        byte b3;  // 1
    }

    private static class ObjectB {
        ObjectC objectC = new ObjectC();
    }

    private static class ObjectC {
        ObjectD[] array = new ObjectD[2];

        public ObjectC() {
            array[0] = new ObjectD();
            array[1] = new ObjectD();
        }
    }

    private static class ObjectD {
        int value;
    }
}
