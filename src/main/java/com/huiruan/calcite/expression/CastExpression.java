package com.huiruan.calcite.expression;

import com.huiruan.calcite.catalog.Type;

import java.util.Collections;

public class CastExpression extends Expression {
    private final Type targetType;

    public CastExpression(Type targetType, Expression input) {
        super(Collections.singletonList(input));
        this.targetType = targetType;
    }
}
