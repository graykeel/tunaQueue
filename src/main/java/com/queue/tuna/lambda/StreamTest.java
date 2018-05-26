package com.queue.tuna.lambda;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by zhangtao on 2017/5/31.
 */
public class StreamTest {
    public static void main(String[] args) throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get("/Volumes/MacintoshHD/workspace/logging-log4j2/README.md")), StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("\\s+"));
//        Long startTime = System.nanoTime();
//        Long count = words.stream().filter(
//                (String w) -> {
//                    if (w.length() > 2)
//                        return true;
//                    else
//                        return false;
//                }).count();
//        System.out.println(System.nanoTime()-startTime);
//        startTime = System.nanoTime();
//        count = words.parallelStream().filter(w->w.length()>2).count();
//        System.out.println(System.nanoTime()-startTime);
//        System.out.println(count);
//        Optional<String> largest = words.stream().max(String::compareToIgnoreCase);
//        System.out.println(largest.get());
        Optional<String> findFirst = words.stream().filter(s -> s.startsWith("L")).findAny();
//        if (findFirst.isPresent()){
//            System.out.println(findFirst.get());
//        }
        List<String> results = new ArrayList<>();
        Stream<String> stream = words.stream().filter(s -> results.add(s));
        findFirst.map(s -> (results.add(s)));
        System.out.println(results);
        System.out.println(stream.toArray().length);
//        Test<Double> test = (Double x)->{
//            return x < 0 ? Optional.empty() : Optional.of(Math.abs(x));
//        };

        List<Integer> integerList = new ArrayList() {
            {
                add(1);
                add(3);
                add(5);
                add(7);
                add(9);
                add(11);
            }
        };
        Stream<Integer> integerStream = integerList.stream();
        Optional<Integer> sumOp = integerStream.reduce((x,y)->x+y);
        System.out.println(sumOp.get());
        sumOp = integerList.stream().reduce(Integer::sum);
        System.out.println(sumOp.get());
    }

    public static Optional<Double> squareRoot(Double x) {
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }
}
