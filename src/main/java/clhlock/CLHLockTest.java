package clhlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangtao on 2017/3/19.
 */
public class CLHLockTest implements Lock {

    AtomicReference<QNode> tail;

    ThreadLocal<QNode> myPred;

    ThreadLocal<QNode> myNode;

    class QNode {
        QNode pred;
        volatile Boolean lockStatus;
    }

    CLHLockTest() {

    }

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {
        final Lock lock = new ReentrantLock();
        final Condition condition1 = lock.newCondition();
        final Condition condition2 = lock.newCondition();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (lock.tryLock()) {
                        try {
                            System.out.println("condition1.await");
                            condition1.await();
                            System.out.println("condition1 weak up");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (lock.tryLock()) {
                        try {
                            System.out.println("condition2.await");
                            condition2.await();
                            System.out.println("condition2 weak up");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (lock.tryLock()) {
                        System.out.println("condition2.await——");
                        condition2.signal();
                        System.out.println("condition2 weak up——");
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (lock.tryLock()) {
                        System.out.println("condition1.await——");
                        condition1.signal();
                        System.out.println("condition1 weak up——");
                    }
                }
            }
        }).start();
    }
}
