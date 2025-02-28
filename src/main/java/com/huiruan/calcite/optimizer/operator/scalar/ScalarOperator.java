package com.huiruan.calcite.optimizer.operator.scalar;

import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.optimizer.operator.OperatorType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public abstract class ScalarOperator implements Cloneable {
    protected final OperatorType opType;

    @Setter
    protected Type type;

    public ScalarOperator(OperatorType opType, Type type) {
        this.opType = opType;
        this.type = type;
    }

    public boolean isConstant() {
        for (ScalarOperator child : getChildren()) {
            if (!child.isConstant()) {
                return false;
            }
        }
        return true;
    }

    public boolean isVariable() {
        for (ScalarOperator child : getChildren()) {
            if (child.isVariable()) {
                return true;
            }
        }
        return false;
    }

    public abstract boolean isNullable();

    public abstract List<ScalarOperator> getChildren();

    public abstract ScalarOperator getChild(int index);

    public abstract void setChild(int index, ScalarOperator child);

    @Override
    public ScalarOperator clone() {
        ScalarOperator operator = null;
        try {
            operator = (ScalarOperator) super.clone();
        } catch (CloneNotSupportedException ignored) {
        }
        return operator;
    }
}
