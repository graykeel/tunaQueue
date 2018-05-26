package com.queue.tuna.lambda;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhangtao on 2017/5/29.
 */
public class Greeter {
    public void greet(){
        System.out.println("Hello,world!");
    }
    public static void main(String[] args){
        ConcurrentGreeter greeter = new ConcurrentGreeter();
        greeter.greet();
        List<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        Stream<Button> stream = labels.stream().map(Button::new);
        List<Button> buttons = stream.collect(Collectors.toList());
        System.out.println(buttons);
        stream = labels.stream().map(Button::new);
        Button[] buttons1 = stream.toArray(Button[]::new);
        System.out.println(buttons1);
    }
}

class ConcurrentGreeter extends Greeter{
    public void greet(){
        Thread thread = new Thread(super::greet);
        thread.start();
    }
}
