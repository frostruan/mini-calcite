package com.huiruan.calcite.analyzer;

import com.huiruan.calcite.catalog.Catalog;
import com.huiruan.calcite.catalog.Database;
import com.huiruan.calcite.catalog.Field;
import com.huiruan.calcite.catalog.Table;
import com.huiruan.calcite.expression.ExpressionUtils;
import com.huiruan.calcite.expression.Identifier;
import com.huiruan.calcite.expression.Predicate;
import com.huiruan.calcite.parser.SQLNode;
import com.huiruan.calcite.parser.SQLNode.QueryStmt;
import com.huiruan.calcite.parser.SQLNode.JoinClause;
import com.huiruan.calcite.parser.SQLNode.SelectStmt;
import com.huiruan.calcite.parser.SQLNode.RelationClause;
import com.huiruan.calcite.parser.SQLNode.TableReference;

import java.util.List;

public final class Analyzer {

    private Analyzer() {}

    public static void analyze(QueryStmt queryStmt, Catalog catalog) {
        Validator validator = new Validator(catalog);
        Scope scope = validator.validate(queryStmt, Scope.ROOT);
    }

    private static final class Validator extends AstVisitor<Scope, Scope> {
        private final Catalog catalog;

        private Validator(Catalog catalog) {
            this.catalog = catalog;
        }

        public Scope validate(SQLNode node, Scope scope) {
            return node.accept(this, scope);
        }

        @Override
        public Scope visitQueryStatement(QueryStmt queryStmt, Scope context) {
            return super.visitQueryStatement(queryStmt, context);
        }

        @Override
        public Scope visitSelectStmt(SelectStmt selectStmt, Scope parent) {
            Scope sourceScope = visitRelationClause(selectStmt.getRelationClause(), parent);
            sourceScope.setParent(parent);
            return super.visitSelectStmt(selectStmt, parent);
        }

        @Override
        public Scope visitRelationClause(RelationClause relationClause, Scope context) {
            TableReference tableRef = relationClause.getTableRef();
            Scope scope = visitTableReference(tableRef, context);

            List<JoinClause> joinClauses = relationClause.getJoinClauses();
            if (joinClauses.isEmpty()) {
                return scope;
            } else {
                if (tableRef.isSubQuery()) {
                    throw new AnalysisException("SubQuery on JOIN is not supported");
                }
                Relation.JoinRelation joinRelation = null;
                Relation leftTable = scope.getRelation();
                for (JoinClause joinClause : joinClauses) {
                    TableReference rightTableRef = joinClause.getRightTable();
                    if (rightTableRef.isSubQuery()) {
                        throw new AnalysisException("SubQuery on JOIN is not supported");
                    }
                    Relation rightTable = visitTableReference(rightTableRef, context).getRelation();
                    joinRelation = new Relation.JoinRelation(leftTable, rightTable,
                            joinClause.getJoinType(), joinClause.getCondition());
                    leftTable = joinRelation;
                }
                return new Scope(context, joinRelation);
            }
        }

        @Override
        public Scope visitTableReference(TableReference tableRef, Scope parent) {
            Relation tableRelation;
            if (tableRef.isSubQuery()) {
                Scope outerScope = visitQueryStatement(tableRef.getQueryStmt(), Scope.ROOT);
                Relation subQuery = new Relation.SubQueryRelation(
                        outerScope.getRelation().getAllFields(), tableRef.getTableAlias().getId());
                return new Scope(parent, subQuery);
            } else {
                Identifier.TableName tableName = tableRef.getTableName();
                Table table = resolveTable(tableName, catalog);
                List<Field> fieldList = table.getFields();
                tableRelation = new Relation.TableRelation(fieldList, tableName, tableRef.getTableAlias());
                return new Scope(parent, tableRelation);
            }
        }

        private Table resolveTable(Identifier.TableName tableName, Catalog catalog) {
            String dbName = ExpressionUtils.getDatabase(tableName);
            Database db = catalog.getDatabase(dbName);
            if (db == null) {
                throw new AnalysisException(
                        String.format("db %s not found in default_catalog: table=%s",
                                dbName, tableName));
            }
            Table table = db.getTable(tableName);
            if (table == null) {
                throw new AnalysisException(
                        String.format("table %s not found in db %s", tableName, dbName));
            }
            return table;
        }

        private void analyzeWhere(SelectStmt selectStmt, Scope context) {
            Predicate whereCondition = selectStmt.getWhereExpr();
            if (whereCondition == null) {
                return;
            }

        }

        @Override
        public Scope visitColumnItem(SQLNode.ColumnItem columnItem, Scope context) {
            return null;
        }

        private void recursivelyAnalyze() {

        }
    }

    public static final class AnalysisException extends RuntimeException {
        public AnalysisException(String msg) {
            super(msg);
        }
    }
}
