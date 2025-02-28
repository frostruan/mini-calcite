package com.huiruan.calcite.catalog;

import com.google.common.collect.Lists;
import com.huiruan.calcite.expression.Identifier;
import lombok.Getter;

import java.util.List;

@Getter
public class Table {
    private final List<Field> fields;
    private final Identifier.TableName tableName;

    public Table(List<Field> fields, Identifier.TableName tableName) {
        this.fields = fields;
        this.tableName = tableName;
    }
}
