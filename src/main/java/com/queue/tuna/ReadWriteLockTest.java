package com.queue.tuna;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhangtao on 2017/5/8.
 */
public class ReadWriteLockTest {
    public static void main(String[] args) throws Exception{
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Unsafe unsafe = AccessController.doPrivileged(new PrivilegedAction<Unsafe>() {

            @Override
            public Unsafe run() {
                try {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                } catch (NoSuchFieldException e) {
                    return null;
                } catch (IllegalAccessException e) {
                    return null;
                }
            }
        });

        AddressTestA a = new AddressTestA();
        AddressTestB first = new AddressTestB();
        a.setA(first);
        a.setB(first);
        AddressTestB second = new AddressTestB();
        Long aOffset = unsafe.objectFieldOffset(AddressTestA.class.getDeclaredField("A"));
        System.out.println(aOffset);
        Long bOffset = unsafe.objectFieldOffset(AddressTestA.class.getDeclaredField("B"));
        System.out.println(bOffset);

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("cyclicBarrier runnable");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    static class AddressTestA {

        private AddressTestB A;

        private AddressTestB B;

        public AddressTestB getA() {
            return A;
        }

        public void setA(AddressTestB a) {
            A = a;
        }

        public AddressTestB getB() {
            return B;
        }

        public void setB(AddressTestB b) {
            B = b;
        }
    }

    static class AddressTestB {

        private Long A;

        private Long B;

        public Long getA() {
            return A;
        }

        public void setA(Long a) {
            A = a;
        }

        public Long getB() {
            return B;
        }

        public void setB(Long b) {
            B = b;
        }
    }

}
