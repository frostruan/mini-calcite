package com.huiruan.calcite.analyzer;

import com.huiruan.calcite.expression.Expression;
import com.huiruan.calcite.expression.Identifier;
import com.huiruan.calcite.parser.SQLNode;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
public final class Scope {
    public static final Scope ROOT = new Scope(null, null);

    @Setter
    private Scope parent;
    private final Relation relation;

    public Scope(Scope parent, Relation relation) {
        this.parent = parent;
        this.relation = relation;
    }

    public void resolveField(SQLNode.ColumnItem columnItem) {
        Identifier.TableAlias tableAlias = columnItem.getTableAlias();
        if (!matchTableName(tableAlias, relation)) {
            throw new Analyzer.AnalysisException("Can not resolve: " + columnItem);
        }
        Expression expression = columnItem.getExpression();

    }

    private static boolean matchTableName(Identifier.TableAlias tableAlias, Relation relation) {
        if (relation.isTableRelation()) {
            Relation.TableRelation tableRelation = (Relation.TableRelation) relation;
            return tableRelation.getTableName().getId().equalsIgnoreCase(tableAlias.getId()) ||
                    tableRelation.getTableAlias().getId().equalsIgnoreCase(tableAlias.getId());
        }
        if (relation.isSubQuery()) {
            return tableAlias.getId().equalsIgnoreCase((relation.getRelationName()));
        }
        Relation.JoinRelation joinRelation = (Relation.JoinRelation) relation;
        return matchTableName(tableAlias, joinRelation.getLeftTable())
                || matchTableName(tableAlias, joinRelation.getRightTable());
    }
}
