package com.huiruan.calcite.expression;

import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.parser.SQLNode;
import lombok.Getter;

import java.util.List;

@Getter
public class Expression extends SQLNode {

    public static final String STAR = "*";

    private Type type;
    private List<Expression> inputs;

    public Expression() {
    }

    public Expression(Type type) {
        this.type = type;
    }

    public Expression(List<Expression> inputs) {
       this.inputs = inputs;
    }

    public Expression(Type type, List<Expression> inputs) {
        this.type = type;
        this.inputs = inputs;
    }
}
