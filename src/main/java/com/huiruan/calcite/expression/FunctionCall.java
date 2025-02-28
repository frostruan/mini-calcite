package com.huiruan.calcite.expression;

import com.huiruan.calcite.catalog.Type;
import lombok.Getter;

import java.util.Collections;

@Getter
public class FunctionCall extends Expression {

    private final FunctionType functionType;

    public FunctionCall(FunctionType functionType, Expression input, Type type) {
        super(type, Collections.singletonList(input));
        this.functionType = functionType;
    }

    public static FunctionCall count(Expression expr) {
        return new FunctionCall(FunctionType.COUNT, expr, Type.LONG);
    }

    public static FunctionCall sum(Expression expr) {
        return new FunctionCall(FunctionType.SUM, expr, Type.LONG);
    }

    public static FunctionCall avg(Expression expr) {
        return new FunctionCall(FunctionType.AVG, expr, Type.DOUBLE);
    }

    public static FunctionCall min(Expression expr) {
        return new FunctionCall(FunctionType.MIN, expr, expr.getType());
    }

    public static FunctionCall max(Expression expr) {
        return new FunctionCall(FunctionType.MAX, expr, expr.getType());
    }

    public enum FunctionType {
        COUNT,
        SUM,
        AVG,
        MIN,
        MAX
    }
}
