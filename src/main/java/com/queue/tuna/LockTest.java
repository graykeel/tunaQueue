package com.queue.tuna;

import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Created by zhangtao on 2017/3/2.
 */
public class LockTest {
    private static Object lock = new Object();
    public static void main(String[] args){
        synchronized (lock){
            synchronized (lock){
                System.out.println("this is synchronized test!");
            }
        }
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.lock();

        reentrantLock.unlock();
        reentrantLock.unlock();
        final int NUMBER_OF_TRIALS = 1000000000;
        int numberOfHits = 0;
        for(int i = 0;i < NUMBER_OF_TRIALS;i++){
            double x = Math.random()*2.0-1;
            double y = Math.random()*2.0-1;
            if(x*x+y*y<=1)
                numberOfHits++;
        }
        double pi = 4.0*numberOfHits/NUMBER_OF_TRIALS;
        System.out.println("PI is "+pi);
    }
}

class Practice4 {
    public static void main(String[] args) {
        //蒙特卡罗模拟
        final int NUMBER_OF_TRIALS = 1000000;
        int numberOfHits = 0;
        for(int i = 0;i < NUMBER_OF_TRIALS;i++){
            double x = Math.random()*2.0-1;
            double y = Math.random()*2.0-1;
            if(x*x+y*y<=1)
                numberOfHits++;
        }
        double pi = 4.0*numberOfHits/NUMBER_OF_TRIALS;
        System.out.println("PI is "+pi);
    }
}