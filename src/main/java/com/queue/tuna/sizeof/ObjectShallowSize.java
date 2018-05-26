package com.queue.tuna.sizeof;

import java.lang.instrument.Instrumentation;

/**
 * Created by zhangtao on 2017/3/4.
 */
public class ObjectShallowSize {
    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation instP){
        inst = instP;
    }

    public static long sizeOf(Object obj){
        return inst.getObjectSize(obj);
    }
}
