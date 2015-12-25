package cn.hero.others;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Fuzhong on 2015/12/25.
 */
public class Lambda {

    public static void main(String[] args) {
        String path = "D:\\Workspace\\Servers";
        Lambda lambda = new Lambda();
        System.out.println("文件过滤 : ");
        lambda.example1(path);
        System.out.println("Lambad : ");
        lambda.example2(path);

        lambda.traversalFind();

        lambda.traversalHandler();

        lambda.userLambdaCreateList();

        lambda.copyLambda();

        lambda.mathFunction();
    }

    public void mathFunction () {
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics iss = primes.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("max = " + iss.getMax());
        System.out.println("min = " + iss.getMin());
        System.out.println("sum = " + iss.getSum());
        System.out.println("avg = " + iss.getAverage());
        System.out.println("count = " + iss.getCount());
        System.out.println(primes.size());
    }

    public void copyLambda () {
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> num = numbers.stream().map(i -> i*i).distinct()
                .collect(Collectors.toList());
        System.out.printf("numbers E2 = %s%n", num);
        List<Integer> num2 = numbers.stream().map(i -> i+1).distinct()
                .collect(Collectors.toList());
        System.out.printf("num+1 = %s%n", num2);
    }

    public void userLambdaCreateList () {
        List<String> list = Arrays.asList("Fuzhong", "Fujiecheng", "ZXhuxu", "WFngkai", "Fiaofan");
        List<String> str = list.stream().filter((s) -> s.length() > 7 )
                .collect(Collectors.toList());
        System.out.printf("list %s%n", list);
        System.out.printf("str %s%n", str);

        String join = list.stream().map(s -> s.toUpperCase())
                .collect(Collectors.joining(","));
        System.out.println(join);
    }

    /**
     * 使用Lambda实现Map 和 Reduce
     */
    public void traversalHandler () {
        List<Integer> list = Arrays.asList(100, 200, 300, 400, 500);
        list.stream().map((s) -> s+.12*100)
                .forEach(System.out::println);

        double total = list.stream().map((s) -> s + .12*100)
                .reduce((sum, s) -> sum + s)
                .get();
        System.out.println("Total = " + total);
    }


    /**
     * 遍历和遍历查找
     */
    public void traversalFind () {
        String [] strings = {"Fuzhong", "Fujiecheng", "ZXhuxu", "WFngkai", "Fiaofan"};
        String str = "Fuzhong";
        List<String> list = new ArrayList<>(0);
        Collections.addAll(list, strings);
        list.forEach(s -> System.out.println(s));

        list.forEach(Lambda::print); // print 方法
        System.out.println("foreach:");
        list.forEach(s -> s.equalsIgnoreCase(str));

        filter(list, (s) -> s.equalsIgnoreCase(str)); // filter 方法

        /**
         * complex Predicate using!
         */
        Predicate<String> startWithJ = (s) -> s.startsWith("F");
        Predicate<String> lengthFour = (s) -> s.length() == 7;

        list.stream().filter(startWithJ.and(lengthFour))
                .forEach((s) -> System.out.println("Name with start 'F' and lenth is 7 :" + s));
    }

    public static void print(String str) {
        System.out.println("print str = " + str);
    }

    public static void filter (List<String> names, Predicate<String> predicate) {
        for(String name : names)  {
            if(predicate.test(name)) {
                System.out.println(name + " ");
            }
        }
    }

    // 文件过滤
    public void example1 (String path) {
        File file = new File(path);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File [] dir0 = file.listFiles(filter);
        for (File f : dir0) {
            System.out.println(f.getName());
        }
    }

    public void example2 (String path) {
        File file = new File(path);
        File [] dirs = file.listFiles((File f) -> f.isDirectory());
        for (File f : dirs) {
            System.out.println(f.getName());
        }
    }

}
