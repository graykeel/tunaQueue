package com.queue.tuna.lambda;

import javafx.event.EventHandler;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by zhangtao on 2017/5/29.
 */
public class FirstLambdatest {
    public static void main(String[] args){
        Comparator<String> compare = (first, second) -> Integer.compare(first.length(), second.length());
        System.out.println(compare.compare("test","this is my first lambda test!"));
        Comparator<String> a = (String first,String second)->{
            if (first.length()>=second.length())
                return 0;
            else
                return -1;
        };
        LambdaTest lambdaTest = ()->{
          System.out.println("this is a test!");
        };
        lambdaTest.test();
        Object obj = lambdaTest;
        lambdaTest.test1();
        Runnable test = lambdaTest::test;
        test.run();
        System.out.println(lambdaTest);
        EventHandler<ActionEvent> listener = event->System.out.println("this is a test!");
        System.out.println(listener);
        Method[] methods = LambdaTest.class.getMethods();
        for (Method method:methods){
            System.out.println(method.getName());
        }
        Class clazz = LambdaTest.class;
        System.out.println(clazz.getSuperclass());
        TestA t = new TestA();
        t.test();
        t.test3();
        Comparator c = Comparator.comparing(String::length);
        System.out.println(c.compare("test","this is a tst"));
        File file = new File("/Users/");
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory())
                    return true;
                return false;
            }
        });
        for (File f:files)
            System.out.println(f.getName());
        FileFilter fi = (File pathname)->{
            if (pathname.isDirectory())
                return true;
            return false;
        };

        FileFilter ft = new FileFilterTest()::accept;
        files = file.listFiles(ft);
        for (File f:files)
            System.out.println(f.getName());

        String[] names = {"zhang","tao","hu"};
        List<Runnable> runners = new ArrayList<>();
        for (String name:names){
            runners.add(()->System.out.println(name));
        }
        System.out.println(runners.get(0));
        for (int i=0;i<names.length;i++){
            int finalI = i;
            runners.add(()->System.out.println(names[finalI]));
        }
        System.out.println(runners.get(0));
    }

}

class FileFilterTest implements FileFilter{

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory())
            return true;
        return false;
    }
}

interface LambdaTest{
    void test();

    default void test1() {
        System.out.println("this is test1 LambdaTest");
    }
}

interface LambdaTest2{
    void test3();

    default void test1(){
        System.out.println("this is test1 LambdaTest2");
    }
}

class TestA implements LambdaTest,LambdaTest2{

    @Override
    public void test() {
        LambdaTest.super.test1();
    }

    @Override
    public void test1() {
        System.out.println("this is test1 TestA");
    }

    @Override
    public void test3() {
        LambdaTest2.super.test1();
    }
}