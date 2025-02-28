package com.huiruan.calcite.optimizer.operator.scalar;

import com.google.common.base.Preconditions;
import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.optimizer.operator.OperatorType;
import lombok.Getter;

import java.util.List;

public class CompoundPredicateOperator extends PredicateOperator {
    public CompoundPredicateOperator(OperatorType operatorType, ScalarOperator... arguments) {
        super(operatorType, arguments);
    }
    /*
    @Getter
    private final CompoundType type;

    public CompoundPredicateOperator(CompoundType compoundType, ScalarOperator... arguments) {
        super(OperatorType.COMPOUND, arguments);
        this.type = compoundType;
    }

    public CompoundPredicateOperator(CompoundType compoundType, List<ScalarOperator> arguments) {
        super(OperatorType.COMPOUND, arguments);
        this.type = compoundType;
    }

    public enum CompoundType {
        AND,
        OR,
        NOT
    }

    public boolean isAnd() {
        return getType() == CompoundType.AND;
    }

    public boolean isOr() {
        return getType() == CompoundType.OR;
    }

    public boolean isNot() {
        return getType() == CompoundType.NOT;
    } */
}
