package com.huiruan.calcite.expression;

import com.huiruan.calcite.catalog.Type;
import lombok.Getter;

@Getter
public class Identifier extends Expression {
    private final String id;

    public Identifier(String id) {
        super(Type.CHAR);
        this.id = id;
    }

    public static final class TableName extends Identifier {

        public TableName(String id) {
            super(id);
        }
    }

    public static final class ColumnName extends Identifier {

        public ColumnName(String id) {
            super(id);
        }
    }

    public static final class Alias extends Identifier {

        public Alias(String id) {
            super(id);
        }
    }

    public static final class TableAlias extends Identifier {

        public TableAlias(String id) {
            super(id);
        }
    }
}
