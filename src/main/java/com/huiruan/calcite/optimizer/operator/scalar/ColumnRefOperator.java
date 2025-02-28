package com.huiruan.calcite.optimizer.operator.scalar;

import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.optimizer.operator.OperatorType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public final class ColumnRefOperator extends ScalarOperator {
    @Getter
    private final int id;
    @Getter
    private final String name;
    @Setter
    private boolean nullable;

    public ColumnRefOperator(int id, Type type, String name, boolean nullable) {
        super(OperatorType.VARIABLE, type);
        this.id = id;
        this.name = requireNonNull(name, "name is null");
        this.nullable = nullable;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public List<ScalarOperator> getChildren() {
        return emptyList();
    }

    @Override
    public ScalarOperator getChild(int index) {
        return null;
    }

    @Override
    public void setChild(int index, ScalarOperator child) {
    }

    @Override
    public String toString() {
        return "$" + id + ": " + name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static boolean equals(List<ColumnRefOperator> lhs, List<ColumnRefOperator> rhs) {
        if (lhs == null || rhs == null) {
            return lhs == null && rhs == null;
        }
        if (lhs == rhs) {
            return true;
        }
        if (lhs.size() != rhs.size()) {
            return false;
        }
        for (int i = 0; i < lhs.size(); ++i) {
            if (!lhs.get(i).equals(rhs.get(i))) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnRefOperator)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final ColumnRefOperator column = (ColumnRefOperator) obj;
        // The column id is unique
        return id == column.id;
    }
}
