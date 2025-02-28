package com.huiruan.calcite.expression;

public class SlotRef extends Expression {
    private final String tableName;
    private final String columnName;

    public SlotRef(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }
}
