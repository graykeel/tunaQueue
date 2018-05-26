package com.queue.tuna.lockstudy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangtao on 2017/3/14.
 */
public class LockTest {
    private static final ReentrantLock lock = new ReentrantLock();
    static final Condition condition = lock.newCondition();
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                LockTest lockTest = new LockTest();
                lockTest.lock.lock();
                try {
                    condition.await();
                    System.out.println("Thread 1 test!");
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                LockTest lockTest2 = new LockTest();
                lockTest2.lock.lock();
                condition.signal();
                try {
                    condition.await();
                    System.out.println("Thread 2 test!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
        thread1.start();
    }
}
