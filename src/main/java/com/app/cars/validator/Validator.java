package com.app.cars.validator;

public sealed interface Validator<T> permits CarValidator{
    void validate(T t);

    static boolean validateExpression(String expression, String regex){ return expression.matches(regex); }
}
