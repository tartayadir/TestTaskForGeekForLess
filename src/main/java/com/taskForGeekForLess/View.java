package com.taskForGeekForLess;

import java.util.List;
import java.util.Map;

public class View {

    public static void viewResultArithmeticExpressions(Map<String, Double> mathExpressions,
                                                                                   List<String> arithmeticExpressions){

        System.out.println("Results of calculating arithmetic expression : ");
        for (String str : arithmeticExpressions){

            System.out.println(str + "=" + mathExpressions.get(str));
        }
    }

    public static void viewArithmeticExpressions(List<String> arithmeticExpressions){

        System.out.println("Arithmetic expressions : ");
        arithmeticExpressions.forEach(System.out::println);
    }
}
