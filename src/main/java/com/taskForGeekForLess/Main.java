package com.taskForGeekForLess;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        List<String> arithmeticExpressions = Controller.inputArithmeticExpressions();
        Map<String, Double> mapArExpr = Controller.checkArithmeticExpressionsIfRightCalculateAndWriteToDB(
                arithmeticExpressions
        );
        View.viewResultArithmeticExpressions(mapArExpr, arithmeticExpressions);


        arithmeticExpressions = Controller.getAllArithmeticExpressionsFromDB();
        View.viewArithmeticExpressions(arithmeticExpressions);

        arithmeticExpressions = Controller.getAllArithmeticExpressionsWithResultFromDB(s -> s > 0);
        View.viewArithmeticExpressions(arithmeticExpressions);
    }
}
