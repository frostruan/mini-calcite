package com.huiruan.calcite.analyzer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.huiruan.calcite.catalog.Field;
import com.huiruan.calcite.expression.Expression;
import com.huiruan.calcite.expression.Identifier;
import com.huiruan.calcite.parser.SQLNode;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class Relation {

    private final List<Field> allFields;

    public Relation(List<Field> allFields) {
        this.allFields = allFields;
    }

    public abstract String getRelationName();

    public boolean isTableRelation() {
        return false;
    }

    public boolean isSubQuery() {
        return false;
    }

    public boolean isJoinRelation() {
        return false;
    }

    @Getter
    public static final class TableRelation extends Relation {
        private final Identifier.TableName tableName;
        private final Identifier.TableAlias tableAlias;

        public TableRelation(List<Field> allFields,
                             Identifier.TableName tableName,
                             Identifier.TableAlias tableAlias) {
            super(allFields);
            this.tableName = tableName;
            this.tableAlias = tableAlias;
        }

        @Override
        public String getRelationName() {
            return tableName.getId();
        }

        @Override
        public boolean isTableRelation() {
            return true;
        }
    }

    @Getter
    public static final class SubQueryRelation extends Relation {
        private final String name;

        public SubQueryRelation(List<Field> allFields, String name) {
            super(allFields);
            this.name = name;
        }

        @Override
        public String getRelationName() {
            return name;
        }

        @Override
        public boolean isSubQuery() {
            return true;
        }
    }

    @Getter
    public static final class JoinRelation extends Relation {
        private final Relation leftTable;
        private final Relation rightTable;
        private final SQLNode.JoinType joinType;
        private final Expression onPredicate;

        public JoinRelation(Relation leftTable, Relation rightTable,
                            SQLNode.JoinType joinType, Expression onPredicate) {
            super(ImmutableList.<Field>builder()
                    .addAll(leftTable.getAllFields())
                    .addAll(rightTable.getAllFields())
                    .build());
            this.leftTable = leftTable;
            this.rightTable = rightTable;
            this.joinType = joinType;
            this.onPredicate = onPredicate;
        }

        @Override
        public String getRelationName() {
            return "";
        }

        @Override
        public boolean isJoinRelation() {
            return true;
        }
    }
}
