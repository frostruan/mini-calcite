package com.huiruan.calcite.catalog;

import com.huiruan.calcite.expression.Expression;
import com.huiruan.calcite.expression.Identifier;
import lombok.Getter;

@Getter
public class Field {
    private final Identifier.ColumnName name;
    private final Type type;

    private final Identifier.TableName tableName;
    private final Expression originExpression;


    public Field(Identifier.ColumnName name, Type type,
                 Identifier.TableName tableName, Expression originExpression) {
        this.name = name;
        this.type = type;
        this.tableName = tableName;
        this.originExpression = originExpression;
    }

    public boolean matchesName(Identifier.ColumnName columnName) {
        return name.getId().equalsIgnoreCase(columnName.getId());
    }

    public Field withTableName(Identifier.TableName newTableName) {
        return new Field(name, type, newTableName, originExpression);
    }

    public Field withTableName(String newTableName) {
        return withTableName(new Identifier.TableName(newTableName));
    }
}
