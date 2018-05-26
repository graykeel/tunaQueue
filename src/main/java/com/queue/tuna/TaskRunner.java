package com.queue.tuna;

import sun.nio.ch.Interruptible;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangtao on 2017/2/21.
 */
interface Task {
    void execute();
}

public class TaskRunner implements Runnable {
    private BlockingQueue<Task> queue;

    public TaskRunner(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                Task task = queue.take();
                task.execute();
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String args[]) throws Exception {
        final InterruptRead2 test = new InterruptRead2();
        Thread t = new Thread() {

            @Override
            public void run() {
                long start = System.currentTimeMillis();
                try {
                    System.out.println("InterruptRead start!");
                    test.execute();
                } catch (InterruptedException e) {
                    System.out.println("InterruptRead end! cost time : " + (System.currentTimeMillis() - start));
                    e.printStackTrace();
                }
            }
        };
        t.start();
        // 先让Read执行3秒
        Thread.sleep(3000);
        // 发出interrupt中断
        t.interrupt();
    }

}

class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {
            /* Allow thread to exit */
        }
    }

    public void cancel() {
        interrupt();
    } // 发起中断
}

interface InterruptAble { // 定义可中断的接口

    public void interrupt() throws InterruptedException;
}

abstract class InterruptSupport implements InterruptAble {

    private volatile boolean interrupted = false;
    private Interruptible interruptor = new Interruptible() {

        public void interrupt(Thread thread) {
//            thread.interrupt();
            interrupted = true;
            this.interrupt(); // 位置3
        }

        public void interrupt() {
            interrupted = true;
            InterruptSupport.this.interrupt(); // 位置3
        }
    };

    public final boolean execute() throws InterruptedException {
        try {
            blockedOn(interruptor); // 位置1
            if (Thread.currentThread().isInterrupted()) { // 立马被interrupted
                interruptor.interrupt(Thread.currentThread());
            }
            // 执行业务代码
            bussiness();
        } finally {
            blockedOn(null);   // 位置2
        }

        return interrupted;
    }

    public abstract void bussiness();

    public abstract void interrupt();

    // -- sun.misc.SharedSecrets --
    static void blockedOn(Interruptible intr) { // package-private
        sun.misc.SharedSecrets.getJavaLangAccess().blockedOn(Thread.currentThread(), intr);
    }
}

class InterruptRead extends InterruptSupport {

    private FileInputStream in;

    @Override
    public void bussiness() {
        File file = new File("/dev/urandom"); // 读取linux黑洞，永远读不完
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            while (in.read(bytes, 0, 1024) > 0) {
                // Thread.sleep(100);
                // if (Thread.interrupted()) {// 以前的Interrupt检查方式
                // throw new InterruptedException("");
                // }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FileInputStream getIn() {
        return in;
    }

    @Override
    public void interrupt() {
        try {
            in.getChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class InterruptRead2 extends InterruptSupport {
    private String test = new String();
    public void bussiness() {
        try{
            while(true){
                test.equals("");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void interrupt() {
        try {
            test = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}