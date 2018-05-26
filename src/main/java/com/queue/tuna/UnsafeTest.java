package com.queue.tuna;

import com.esotericsoftware.reflectasm.ConstructorAccess;
//import org.apache.commons.beanutils.BeanUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 2017/2/26.
 */
public class UnsafeTest {

    public static void main(String[] args) throws Exception {
//        Unsafe unsafe = Unsafe.getUnsafe();
        Unsafe UNSAFE = AccessController.doPrivileged(new PrivilegedAction<Unsafe>(){

            @Override
            public Unsafe run() {
                try {
                    Field field = Unsafe.class.getDeclaredField("theUnsafe");
                    field.setAccessible(true);
                    return (Unsafe) field.get(null);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Map<String,Long> timeMap = new HashMap<String, Long>();
        long totalTime = 0;
        long startNanos = 0;
        Field[] fields = OffsetTest.class.getDeclaredFields();
        for (int i=0;i<1000000;i++){
            OffsetTest test = new OffsetTest();
            test.setOffset1(1);
            test.setOffset2(2);
            test.setOffset3(3L);
            test.setOffset4(4L);
            test.setOffset5(5);
            test.setOffset6(6);
            startNanos = System.nanoTime();
            OffsetTest test1 = new OffsetTest();
            test1.setOffset1(test.getOffset1());
            test1.setOffset2(test.getOffset2());
            test1.setOffset3(test.getOffset3());
            test1.setOffset4(test.getOffset4());
            test1.setOffset5(test.getOffset5());
            test1.setOffset6(test.getOffset6());
//            System.out.println("COPY   :"+(System.nanoTime()-startNanos));
            totalTime = (timeMap.get("copyTime")==null?0:timeMap.get("copyTime"))+System.nanoTime()-startNanos;
            timeMap.put("copyTime",totalTime);
            startNanos = System.nanoTime();
            long offset1 = UNSAFE.objectFieldOffset
                    (OffsetTest.class.getDeclaredField("offset1"));
//        System.out.println("offset1:"+offset1);

            long offset2 = UNSAFE.objectFieldOffset
                    (OffsetTest.class.getDeclaredField("offset2"));
//        System.out.println("offset2:"+offset2);

            long offset3 = UNSAFE.objectFieldOffset
                    (OffsetTest.class.getDeclaredField("offset3"));
//        System.out.println("offset3:"+offset3);

            long offset4 = UNSAFE.objectFieldOffset
                    (OffsetTest.class.getDeclaredField("offset4"));
//        System.out.println("offset4:"+offset4);

            long offset5 = UNSAFE.objectFieldOffset
                    (OffsetTest.class.getDeclaredField("offset5"));
//        System.out.println("offset5:"+offset5);

            long offset6 = UNSAFE.objectFieldOffset
                    (OffsetTest.class.getDeclaredField("offset6"));
//        System.out.println("offset6:"+offset6);
            Object object = OffsetTest.class.newInstance();
            UNSAFE.putObject(test,offset1,11);
//        System.out.println("OffsetTest#offset1():"+test.getOffset1());
            UNSAFE.putObject(test,offset2,12);
//        System.out.println("OffsetTest#offset2():"+test.getOffset2());
            UNSAFE.putObject(test,offset3,new Long(13L));
//        System.out.println("OffsetTest#offset3():"+test.getOffset3());
            UNSAFE.putObject(test,offset4,new Long(14L));
//        System.out.println("OffsetTest#offset4():"+test.getOffset4());
            UNSAFE.putInt(test,offset5,15);
//        System.out.println("OffsetTest#offset5():"+test.getOffset5());
            UNSAFE.putLong(test,offset6,16);
//        System.out.println("OffsetTest#offset6():"+test.getOffset6());
//            System.out.println("UNSAFE :"+(System.nanoTime()-startNanos));
            totalTime = (timeMap.get("UNSAFETime")==null?0:timeMap.get("UNSAFETime"))+System.nanoTime()-startNanos;
            timeMap.put("UNSAFETime",totalTime);
            startNanos = System.nanoTime();
            if (i==1){
                fields = test.getClass().getDeclaredFields();
            }
            ConstructorAccess<OffsetTest> access = ConstructorAccess.get(OffsetTest.class);
            Object o1 = access.newInstance();
//            Object o1 = OffsetTest.class.newInstance();
            NewBeanUtils.copyProperties(o1,test);
//            for (Field field:fields){
//                field.setAccessible(true);
//
////            field.set(test,o);
//                if ("Integer".equals(field.getType().getSimpleName())){
//                    field.set(test,new Integer(1));
//                }
//                if ("Long".equals(field.getType().getSimpleName())){
//                    field.set(test,new Long(2));
//                }
//                if ("int".equals(field.getType().getSimpleName())){
//                    field.set(test,new Integer(1));
//                }
//                if ("long".equals(field.getType().getSimpleName())){
//                    field.set(test,new Long(2));
//                }
//            }
//            System.out.println("Reflect:"+(System.nanoTime()-startNanos));
            totalTime = (timeMap.get("ReflectTime")==null?0:timeMap.get("ReflectTime"))+System.nanoTime()-startNanos;
            timeMap.put("ReflectTime",totalTime);
        }
        for (String key:timeMap.keySet()){
            System.out.println(key+":"+(Double.valueOf(timeMap.get(key))/1000/100000));
        }

        int[] intOffsetTest = new int[]{1,2,3,4,5,6};
        int intBaseOffset = UNSAFE.arrayBaseOffset(intOffsetTest.getClass());
        int intArrayIndexScale = UNSAFE.arrayIndexScale(intOffsetTest.getClass());
//        System.out.println(System.currentTimeMillis());
//        System.out.println(System.nanoTime());
    }

}

class OffsetTest{
    private Integer offset1;
    private Integer offset2;
    private Long offset3;
    private Long offset4;
    private int offset5;
    private long offset6;

    public Integer getOffset1() {
        return offset1;
    }

    public void setOffset1(Integer offset1) {
        this.offset1 = offset1;
    }

    public Integer getOffset2() {
        return offset2;
    }

    public void setOffset2(Integer offset2) {
        this.offset2 = offset2;
    }

    public Long getOffset3() {
        return offset3;
    }

    public void setOffset3(Long offset3) {
        this.offset3 = offset3;
    }

    public Long getOffset4() {
        return offset4;
    }

    public void setOffset4(Long offset4) {
        this.offset4 = offset4;
    }

    public int getOffset5() {
        return offset5;
    }

    public void setOffset5(int offset5) {
        this.offset5 = offset5;
    }

    public long getOffset6() {
        return offset6;
    }

    public void setOffset6(long offset6) {
        this.offset6 = offset6;
    }
}
