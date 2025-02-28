package com.huiruan.calcite.parser;

import com.huiruan.calcite.analyzer.AstVisitor;
import com.huiruan.calcite.expression.Expression;
import com.huiruan.calcite.expression.Identifier;
import com.huiruan.calcite.expression.Predicate;
import com.huiruan.calcite.utils.Pair;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class SQLNode {

    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
       return visitor.visit(this, context);
    }

    public enum UnionType {
        UNION, UNION_ALL
    }

    public enum SortOrder { ASC, DESC }

    public enum JoinType { INNER, LEFT, RIGHT, FULL }

    @Getter
    public static final class QueryStmt extends SQLNode {
        public static final int NO_LIMIT = -1;
        private final int limit;
        private final SelectStmt selectStmt;
        private final List<Pair<UnionType, SelectStmt>> selectStmtList;

        public QueryStmt(SelectStmt selectStmt) {
            this(selectStmt, NO_LIMIT);
        }

        public QueryStmt(SelectStmt selectStmt, int limit) {
            this(selectStmt, Collections.emptyList(), limit);
        }

        public QueryStmt(SelectStmt selectStmt, List<Pair<UnionType, SelectStmt>> selectStmtList, int limit) {
            this.limit = limit;
            this.selectStmt = selectStmt;
            this.selectStmtList = selectStmtList;
        }

        @Override
        public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
            return visitor.visitQueryStatement(this, context);
        }
    }

    @Getter
    public static final class SelectStmt extends SQLNode {
        private final boolean distinct;
        private final List<ColumnItem> columnItemList;
        private final RelationClause relationClause;
        private final Predicate whereExpr;
        private final List<Expression> groupByExprs;
        private final Predicate havingExpr;
        private final List<OrderByItem> orderByItems;

        public SelectStmt(boolean distinct, List<ColumnItem> columnItemList,
                          RelationClause relationClause,
                          Predicate whereExpr, List<Expression> groupByExprs,
                          Predicate havingExpr, List<OrderByItem> orderByItems) {
            this.distinct = distinct;
            this.columnItemList = columnItemList;
            this.relationClause = relationClause;
            this.whereExpr = whereExpr;
            this.groupByExprs = groupByExprs;
            this.havingExpr = havingExpr;
            this.orderByItems = orderByItems;
        }
    }

    @Getter
    public static final class RelationClause extends SQLNode {
        private final TableReference tableRef;
        private final List<JoinClause> joinClauses;

        public RelationClause(TableReference tableRef) {
            this(tableRef, Collections.emptyList());
        }

        public RelationClause(TableReference tableRef, List<JoinClause> joinClauses) {
            this.tableRef = tableRef;
            this.joinClauses = joinClauses;
        }
    }

    @Getter
    public static final class ColumnItem extends SQLNode {
        private final Expression expression;
        private final Identifier.Alias alias;
        private final Identifier.TableAlias tableAlias;

        public ColumnItem(Expression expression) {
            this(expression, null, null);
        }

        public ColumnItem(Expression expression, Identifier.Alias alias) {
            this(expression, alias, null);
        }

        public ColumnItem(Expression expression, Identifier.TableAlias tableAlias) {
            this(expression, null, tableAlias);
        }

        public ColumnItem(Expression expression, Identifier.Alias alias, Identifier.TableAlias tableAlias) {
            this.expression = expression;
            this.alias = alias;
            this.tableAlias = tableAlias;
        }
    }

    @Getter
    public static final class OrderByItem extends SQLNode {
        private final Expression expression;
        private final SortOrder sortOrder;

        public OrderByItem(Expression expression) {
            this(expression, SortOrder.ASC);
        }

        public OrderByItem(Expression expression, SortOrder sortOrder) {
            this.expression = expression;
            this.sortOrder = sortOrder;
        }
    }

    @Getter
    public static final class JoinClause extends SQLNode {
        private final JoinType joinType;
        private final TableReference rightTable;
        private final Predicate condition;

        public JoinClause(JoinType joinType, TableReference rightTable, Predicate condition) {
            this.joinType = joinType;
            this.rightTable = rightTable;
            this.condition = condition;
        }
    }

    @Getter
    public static final class TableReference extends SQLNode {
        private final Identifier.TableName tableName;
        private final QueryStmt queryStmt;
        private final Identifier.TableAlias tableAlias;

        private TableReference(Identifier.TableName tableName,
                               QueryStmt queryStmt,
                               Identifier.TableAlias tableAlias) {
            this.tableName = tableName;
            this.queryStmt = queryStmt;
            this.tableAlias = tableAlias;
        }

        public static TableReference of(Identifier.TableName tableName, Identifier.TableAlias alias) {
            return new TableReference(tableName, null, alias);
        }

        public static TableReference of(QueryStmt queryStmt, Identifier.TableAlias alias) {
            return new TableReference(null, queryStmt, alias);
        }

        public boolean isSubQuery() {
            return queryStmt != null;
        }
    }
}
