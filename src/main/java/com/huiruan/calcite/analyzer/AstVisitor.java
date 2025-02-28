package com.huiruan.calcite.analyzer;

import com.huiruan.calcite.expression.Identifier;
import com.huiruan.calcite.parser.SQLNode;
import com.huiruan.calcite.parser.SQLNode.ColumnItem;
import com.huiruan.calcite.parser.SQLNode.JoinClause;
import com.huiruan.calcite.parser.SQLNode.JoinType;
import com.huiruan.calcite.parser.SQLNode.OrderByItem;
import com.huiruan.calcite.parser.SQLNode.SelectStmt;
import com.huiruan.calcite.parser.SQLNode.SortOrder;
import com.huiruan.calcite.parser.SQLNode.RelationClause;
import com.huiruan.calcite.parser.SQLNode.UnionType;
import com.huiruan.calcite.parser.SQLNode.TableReference;
import com.huiruan.calcite.parser.SQLNode.QueryStmt;


public class AstVisitor<R, C> {
    public R visit(SQLNode node, C context) {
        return node.accept(this, context);
    }

    public R visitQueryStatement(QueryStmt queryStmt, C context) {
        return visit(queryStmt, context);
    }

    public R visitSelectStmt(SelectStmt selectStmt, C context) {
        return visit(selectStmt, context);
    }

    public R visitRelationClause(RelationClause relationClause, C context) {
        return visit(relationClause, context);
    }

    public R visitColumnItem(ColumnItem columnItem, C context) {
        return visit(columnItem, context);
    }

    public R visitTableReference(TableReference tableRef, C context) {
        return visit(tableRef, context);
    }

    public R visitJoinClause(JoinClause joinClause, C context) {
        return visit(joinClause, context);
    }

    public R visitColumnName(Identifier.ColumnName columnName, C context) {
        return visit(columnName, context);
    }
}
