package com.huiruan.calcite.optimizer.operator.scalar;

import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.optimizer.operator.OperatorType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ConstantOperator extends ScalarOperator implements Comparable<ConstantOperator> {

    private final Object value;
    private final boolean isNull;

    private ConstantOperator(Type type) {
        super(OperatorType.CONSTANT, type);
        this.value = null;
        this.isNull = true;
    }

    public ConstantOperator(Object value, Type type) {
        super(OperatorType.CONSTANT, type);
        Objects.requireNonNull(value, "constant value is null");
        this.value = value;
        this.isNull = false;
    }

    @Override
    public boolean isNullable() {
        return isNull || type == Type.NULL;
    }

    @Override
    public List<ScalarOperator> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public ScalarOperator getChild(int index) {
        return null;
    }

    @Override
    public void setChild(int index, ScalarOperator child) {
    }

    @Override
    public int compareTo(ConstantOperator o) {
        return 0;
    }

    public static ConstantOperator createBoolean(boolean value) {
        return new ConstantOperator(value, Type.BOOLEAN);
    }

    public static ConstantOperator createLong(long value) {
        return new ConstantOperator(value, Type.LONG);
    }

    public static ConstantOperator createDouble(double value) {
        return new ConstantOperator(value, Type.DOUBLE);
    }

    public static ConstantOperator createChar(String value) {
        return new ConstantOperator(value, Type.CHAR);
    }

    public static ConstantOperator createDate(LocalDateTime value) {
        return new ConstantOperator(value, Type.DATE);
    }

    public static ConstantOperator createTimestamp(long value) {
        return new ConstantOperator(value, Type.TIMESTAMP);
    }

    public static ConstantOperator createNull() {
        return new ConstantOperator(Type.NULL);
    }

    public boolean getBoolean() {
        return (boolean) Optional.ofNullable(value).orElse(false);
    }

    public long getLong() {
        return (long) Optional.ofNullable(value).orElse(0L);
    }

    public double getDouble() {
        return (double) Optional.ofNullable(value).orElse(0.0);
    }

    public String getChar() {
        return (String) Optional.ofNullable(value).orElse("");
    }

    public LocalDateTime getDate() {
        return (LocalDateTime) Optional.ofNullable(value).orElse(LocalDateTime.MIN);
    }

    public long getTimestamp() {
        return (long) Optional.ofNullable(value).orElse(0L);
    }
}
