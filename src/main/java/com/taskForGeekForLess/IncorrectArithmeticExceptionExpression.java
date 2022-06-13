package com.taskForGeekForLess;

public class IncorrectArithmeticExceptionExpression extends Exception {
    public IncorrectArithmeticExceptionExpression(String message) {
        super(message);
    }

    public IncorrectArithmeticExceptionExpression(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectArithmeticExceptionExpression(Throwable cause) {
        super(cause);
    }

    public IncorrectArithmeticExceptionExpression(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IncorrectArithmeticExceptionExpression() {
        super();
    }
}
