package com.huiruan.calcite.expression;

import com.huiruan.calcite.catalog.Type;
import lombok.Getter;

import java.sql.Date;

@Getter
public class Literal extends Expression {

    private final Object value;

    public Literal() {
        this(null, Type.NULL);
    }

    public Literal(boolean value) {
        this(value, Type.BOOLEAN);
    }

    public Literal(long value) {
        this(value, Type.LONG);
    }

    public Literal(double value) {
        this(value, Type.DOUBLE);
    }

    public Literal(String value) {
        this(value, Type.CHAR);
    }

    public Literal(Date value) {
        this(value, Type.DATE);
    }

    private Literal(Object value, Type type) {
        super(type);
        this.value = value;
    }
}
