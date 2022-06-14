package com.taskForGeekForLess;

import java.sql.*;
import java.util.*;
import java.util.function.Predicate;

import static com.taskForGeekForLess.LexemeType.*;

public class Controller {

    private static String DBUrl = "jdbc:mysql://localhost:3306/matsexpressions";
    private static String user = "root";
    private static String password = "root";
    private static String columnLabelWithArithmeticExpression = "mathExpression";
    private static String columnLabelWithResult = "value";
    private static String DBName = "arethmeticexpression";

    public static List<String> inputArithmeticExpressions(){

        Scanner consoleScanner = new Scanner(System.in);
        List<String> expressions = new ArrayList<>();
        int numberOfArithmeticExpr;
        String arExpr;

        System.out.print("Enter number of arithmetic expressions : ");
        numberOfArithmeticExpr = consoleScanner.nextInt();

        System.out.println("Enter arithmetic expressions : ");
        for (int i = 0; i < numberOfArithmeticExpr; i++){

            arExpr = consoleScanner.nextLine();
            if (arExpr.length() > 0) {
                expressions.add(arExpr);
            }else{
                --i;
            }
        }

        return expressions;
    }

    public static List<String> getAllArithmeticExpressionsWithResultFromDB(Predicate<Double> predicate){

        List<String> mathExpressions = new ArrayList<>();
        String mathExpression;
        double result;

        try(Connection connectionToDataBase = DriverManager.getConnection(
                DBUrl,user,password);
            Statement statement = connectionToDataBase.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM " +  DBName + " ;"
                    );

            while (resultSet.next()){

                mathExpression = resultSet.getString(columnLabelWithArithmeticExpression);
                result = resultSet.getDouble(columnLabelWithResult);

                if (predicate.test(result)) {
                    mathExpressions.add(mathExpression);
                }
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return mathExpressions;
    }

    public static List<String> getAllArithmeticExpressionsFromDB(){

        List<String> mathExpressions = new ArrayList<>();
        String mathExpression;

        try(Connection connectionToDataBase = DriverManager.getConnection(
                DBUrl,user,password);
            Statement statement = connectionToDataBase.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT mathExpression FROM arethmeticexpression");

            while (resultSet.next()){

                mathExpression = resultSet.getString(columnLabelWithArithmeticExpression);
                mathExpressions.add(mathExpression);
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return mathExpressions;
    }

    public static Map<String, Double> checkArithmeticExpressionsIfRightCalculateAndWriteToDB
            (List<String> arithmeticExpressions){

        Map<String, Double> mathExpressions = new HashMap<>();
        Double result;
        String mathExpression;

        try(Connection connectionToDataBase = DriverManager.getConnection(
                DBUrl,user,password);
            Statement statement = connectionToDataBase.createStatement()) {

            for (String arExpr : arithmeticExpressions){

                mathExpression = arExpr;
                try {
                    result = expr(parseMathExpression(mathExpression));
                    statement.executeUpdate(
                            "INSERT INTO " + DBName + " SET " +
                            columnLabelWithArithmeticExpression + "='" + mathExpression + "', " +
                            columnLabelWithResult + "=" + result + ";"

                    );
                }catch (IncorrectArithmeticExceptionExpression incorrectArithmeticExceptionExpression){
                    result = null;
                }
                mathExpressions.put(mathExpression, result);
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return mathExpressions;
    }

    public static Map<String, Double> calculateArithmeticExpressions(List<String> arithmeticExpressions){

        Map<String, Double> mathExpressions = new HashMap<>();
        Double result;
        String mathExpression;

        for (String arExpr : arithmeticExpressions){

            mathExpression = arExpr;
            try {
                result = expr(parseMathExpression(mathExpression));
            }catch (IncorrectArithmeticExceptionExpression incorrectArithmeticExceptionExpression){
                result = null;
            }
            mathExpressions.put(mathExpression, result);
        }

        return mathExpressions;
    }

    public static Double calculateArithmeticExpression(String arithmeticExpression){

        Double result = (double) 0;

        try {
            result = expr(parseMathExpression(arithmeticExpression));
        }catch (IncorrectArithmeticExceptionExpression iaee){
            iaee.printStackTrace();
        }

        return result;
    }

    public static boolean checkArithmeticExpression(String arithmeticExpression){

        try {
            expr(parseMathExpression(arithmeticExpression));
        }catch (IncorrectArithmeticExceptionExpression iaee){
            return false;
        }

        return true;
    }

    public static boolean checkCorrectnessClosedBrackets(String arithmeticExpression){

        long counterRegularBrackets = 0;
        long counterInverseBrackets = 0;

        String[] brackets = arithmeticExpression.split("");
        for (String str : brackets){

            if (counterInverseBrackets > counterRegularBrackets){
                return false;
            }
            if (str.equals("(")){
                counterRegularBrackets++;
            }if(str.equals(")")){
                counterInverseBrackets++;
            }
        }

        return counterInverseBrackets == counterRegularBrackets;
    }

    private static Lexemes parseMathExpression(String mathExpression) throws IncorrectArithmeticExceptionExpression{

        List<Lexeme> lexemes = new ArrayList<>();
        String[] lexemesArr = mathExpression.replaceAll("\\s","").split("");
        int counterMultiDivChar = 0;
        int counterRegularBrackets = 0;
        int counterInverseBrackets = 0;

        for(int i = 0; i < lexemesArr.length; i++){

            if (counterInverseBrackets > counterRegularBrackets){
                throw new IncorrectArithmeticExceptionExpression();
            }

            char c = lexemesArr[i].charAt(0);
            switch (c) {
                case '(':
                    counterMultiDivChar = 0;
                    lexemes.add(new Lexeme(LEFT_BRACKET, c));
                    counterRegularBrackets++;
                    continue;
                case ')':
                    counterMultiDivChar = 0;
                    lexemes.add(new Lexeme(RIGHT_BRACKET, c));
                    counterInverseBrackets++;
                    continue;
                case '+':
                    counterMultiDivChar = 0;
                    lexemes.add(new Lexeme(OP_PLUS, c));
                    continue;
                case '*':
                    counterMultiDivChar++;
                    lexemes.add(new Lexeme(OP_MUL, c));
                    continue;
                case '/':
                    counterMultiDivChar++;
                    lexemes.add(new Lexeme(OP_DIV, c));
                    continue;
                default:
                    if (c <= '9' && c >= '0' || c == '-' && counterMultiDivChar>0){

                        int countPoint = 0;
                        StringBuilder number = new StringBuilder();
                        if (c == '-' && counterMultiDivChar>0){
                            c = lexemesArr[++i].charAt(0);
                            number.append("-");
                        }
                        do {
                            if (c == '.') {countPoint++;}
                            number.append(c);
                            i++;
                            if (i >= lexemesArr.length) {break;}
                            if (c == '.' && countPoint > 1) {
                                throw new IncorrectArithmeticExceptionExpression(
                                        "Incorrect char in arithmetic expression"
                                );
                            }
                            c = lexemesArr[i].charAt(0);
                        } while (c <= '9' && c >= '0' || (c == '.' && countPoint < 1));

                        i--;
                        lexemes.add(new Lexeme(NUMBER, number.toString()));

                        counterMultiDivChar = 0;
                    } else {

                        if (c == '-'){
                            counterMultiDivChar = 0;
                            lexemes.add(new Lexeme(OP_MINUS, c));
                        }else {
                            throw new IncorrectArithmeticExceptionExpression("Incorrect char in arithmetic expression");
                        }
                    }

            }
        }

        lexemes.add(new Lexeme(EOF, ""));
        if (counterInverseBrackets != counterRegularBrackets){
            throw new IncorrectArithmeticExceptionExpression();
        }
        return new Lexemes(lexemes);
    }

    private static Double expr(Lexemes lexemes) throws IncorrectArithmeticExceptionExpression{

        Lexeme lexeme = lexemes.nextLexeme();
        if (lexeme.getLexemeType() == LexemeType.EOF) {
            return (double) 0;
        } else {
            lexemes.reducePosition();
            return plusMinus(lexemes);
        }
    }

    private static Double plusMinus(Lexemes lexemes) throws IncorrectArithmeticExceptionExpression{

        double result = multiDiv(lexemes);
        Lexeme lexeme;

        while (true){

            lexeme = lexemes.nextLexeme();
            switch (lexeme.getLexemeType()){
                case OP_MINUS:
                    result -= multiDiv(lexemes);
                    break;
                case OP_PLUS:
                    result += multiDiv(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                    lexemes.reducePosition();
                    return result;
                default:
                    throw new IncorrectArithmeticExceptionExpression("Incorrect char in arithmetic expression");
            }
        }
    }

    private static Double multiDiv(Lexemes lexemes) throws IncorrectArithmeticExceptionExpression{

        Double result = factor(lexemes);
        Lexeme lexeme;

        while (true){

            lexeme = lexemes.nextLexeme();
            switch (lexeme.getLexemeType()){
                case OP_MUL:
                    result *= factor(lexemes);
                    break;
                case OP_DIV:
                    result /= factor(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case OP_PLUS:
                case OP_MINUS:
                    lexemes.reducePosition();
                    return result;
                default:
                    throw new IncorrectArithmeticExceptionExpression("Incorrect char in arithmetic expression");
            }
        }
    }

    private static Double factor(Lexemes lexemes) throws IncorrectArithmeticExceptionExpression{

        Lexeme lexeme = lexemes.nextLexeme();
        switch (lexeme.getLexemeType()){
            case NUMBER:
                return Double.parseDouble(lexeme.getLexemeValue());
            case LEFT_BRACKET:
                Double result = expr(lexemes);
                lexeme = lexemes.nextLexeme();

                if (lexeme.getLexemeType() != RIGHT_BRACKET){
                    throw new IncorrectArithmeticExceptionExpression("Brackets not closed");
                }
                return result;
            default:
                throw new IncorrectArithmeticExceptionExpression("Incorrect char in arithmetic expression");
        }
    }

    public static String getDBUrl() {return DBUrl;}
    public static void setDBUrl(String DBUrl) {Controller.DBUrl = DBUrl;}

    public static String getUser() {return user;}
    public static void setUser(String user) {Controller.user = user;}

    public static String getPassword() {return password;}
    public static void setPassword(String password) {Controller.password = password;}

    public static String getColumnLabelWithArithmeticExpression() {return columnLabelWithArithmeticExpression;}
    public static void setColumnLabelWithArithmeticExpression(String columnLabelWithArithmeticExpression) {
        Controller.columnLabelWithArithmeticExpression = columnLabelWithArithmeticExpression;}

    public static String getColumnLabelWithResult() {return columnLabelWithResult;}
    public static void setColumnLabelWithResult(String columnLabelWithResult) {
        Controller.columnLabelWithResult = columnLabelWithResult;}
}
