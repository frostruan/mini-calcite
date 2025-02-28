package com.huiruan.calcite.expression;

import com.huiruan.calcite.utils.Pair;

public final class ExpressionUtils {

    private ExpressionUtils() {
    }

    public static String getDatabase(Identifier.TableName tableName) {
        if (tableName.getId().contains(".")) {
            return tableName.getId().split("\\.")[0];
        } else {
            return "default";
        }
    }

    public static Pair<Identifier.TableName, String> parseColumnName(Identifier.ColumnName columnName) {
        String[] tokens = columnName.getId().split("\\.", 2);
        assert tokens.length <= 2;
        if (tokens.length == 1) {
            return Pair.create(null, tokens[0]);
        } else {
            return Pair.create(new Identifier.TableName(tokens[0]), tokens[1]);
        }
    }
}
