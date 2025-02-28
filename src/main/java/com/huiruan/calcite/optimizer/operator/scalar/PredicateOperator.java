package com.huiruan.calcite.optimizer.operator.scalar;

import com.google.common.collect.Lists;
import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.optimizer.operator.OperatorType;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class PredicateOperator extends ScalarOperator {
    private List<ScalarOperator> arguments;

    public PredicateOperator(OperatorType operatorType, ScalarOperator... arguments) {
        this(operatorType, Lists.newArrayList(arguments));
    }

    public PredicateOperator(OperatorType operatorType, List<ScalarOperator> arguments) {
        super(operatorType, Type.BOOLEAN);
        this.arguments = requireNonNull(arguments, "arguments is null");
    }

    public List<ScalarOperator> getChildren() {
        return arguments;
    }

    @Override
    public ScalarOperator getChild(int index) {
        return arguments.get(index);
    }

    @Override
    public void setChild(int index, ScalarOperator child) {
        arguments.set(index, child);
    }

    @Override
    public boolean isNullable() {
        return arguments.stream().anyMatch(ScalarOperator::isNullable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opType, arguments);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PredicateOperator other = (PredicateOperator) obj;
        return Objects.equals(this.arguments, other.arguments);
    }

    @Override
    public ScalarOperator clone() {
        PredicateOperator operator = (PredicateOperator) super.clone();
        // Deep copy here
        List<ScalarOperator> newArguments = Lists.newArrayList();
        this.arguments.forEach(p -> newArguments.add(p.clone()));
        operator.arguments = newArguments;
        return operator;
    }
}
